# Quarkus-CXF-FileUpload
Demo repository showing problem with sending big files with Quarkus CXF client/server implementation.


## To reproduce

Start server
```
cd file-service
mvn clean package
quakus dev
```


Start client
```
cd file-client
mvn clean package
quakus dev
```


Call rest endpoint on client with working (small sized) file
```
curl -X POST localhost:8091/files/upload/file-sample_3kB.pdf
```
will return 
```
Successfully uploaded + 3028 bytes
```

Call rest endpoint on client with non-working (medium sized) file
```
curl -X POST localhost:8091/files/upload/file-sample_150kB.pdf
```
will (eventually) return 
```
{"details":"Error id cd79e375-9bb3-4168-b070-2fc830a4405a-1, org.jboss.resteasy.spi.UnhandledException: jakarta.xml.ws.soap.SOAPFaultException: Marshalling Error: Read end dead","stack":"org.jboss.resteasy.spi.UnhandledException: jakarta.xml.ws.soap.SOAPFaultException: Marshalling Error: Read end dead\n\tat org.jboss.resteasy.core.ExceptionHandler.handleApplicationException(ExceptionHandler.java:107)\n\tat org.jboss.resteasy.core.ExceptionHandler.handleException(ExceptionHandler.java:344)\n\tat org.jboss.resteasy.core.SynchronousDispatcher.writeException(SynchronousDispatcher.java:205)\n\tat org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:452)\n\tat org.jboss.resteasy.core.SynchronousDispatcher.lambda$invoke$4(SynchronousDispatcher.java:240)\n\tat org.jboss.resteasy.core.SynchronousDispatcher.lambda$preprocess$0(SynchronousDispatcher.java:154)\n\tat org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:321)\n\tat org.jboss.resteasy.core.SynchronousDispatcher.preprocess(SynchronousDispatcher.java:157)\n\tat org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:229)\n\tat io.quarkus.resteasy.runtime.standalone.RequestDispatcher.service(RequestDispatcher.java:82)\n\tat io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:147)\n\tat io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$1.run(VertxRequestHandler.java:93)\n\tat io.quarkus.vertx.core.runtime.VertxCoreRecorder$14.runWith(VertxCoreRecorder.ja.....
```

with error on server:
```
Caused by: io.netty.handler.codec.http2.Http2Exception: Flow control window exceeded for stream: 0
        at io.netty.handler.codec.http2.Http2Exception.connectionError(Http2Exception.java:109)
        at io.netty.handler.codec.http2.Http2Exception.streamError(Http2Exception.java:152)
        at io.netty.handler.codec.http2.DefaultHttp2LocalFlowController$DefaultState.receiveFlowControlledFrame(DefaultHttp2LocalFlowController.java:429)
        at io.netty.handler.codec.http2.DefaultHttp2LocalFlowController.receiveFlowControlledFrame(DefaultHttp2LocalFlowController.java:273)
        at io.netty.handler.codec.http2.DefaultHttp2ConnectionDecoder$FrameReadListener.onDataRead(DefaultHttp2ConnectionDecoder.java:294)
        at io.netty.handler.codec.http2.DefaultHttp2FrameReader.readDataFrame(DefaultHttp2FrameReader.java:415)
        at io.netty.handler.codec.http2.DefaultHttp2FrameReader.processPayloadState(DefaultHttp2FrameReader.java:250)
        at io.netty.handler.codec.http2.DefaultHttp2FrameReader.readFrame(DefaultHttp2FrameReader.java:159)
        at io.netty.handler.codec.http2.DefaultHttp2ConnectionDecoder.decodeFrame(DefaultHttp2ConnectionDecoder.java:173)
        at io.netty.handler.codec.http2.DecoratingHttp2ConnectionDecoder.decodeFrame(DecoratingHttp2ConnectionDecoder.java:63)
        at io.netty.handler.codec.http2.Http2ConnectionHandler$FrameDecoder.decode(Http2ConnectionHandler.java:393)
```

and client:
```
Caused by: jakarta.xml.bind.MarshalException
 - with linked exception:
[java.io.IOException: Read end dead]
        at org.glassfish.jaxb.runtime.v2.runtime.MarshallerImpl.write(MarshallerImpl.java:268)
        at org.glassfish.jaxb.runtime.v2.runtime.MarshallerImpl.marshal(MarshallerImpl.java:197)
        at jakarta.xml.bind.helpers.AbstractMarshallerImpl.marshal(AbstractMarshallerImpl.java:86)
        at org.apache.cxf.jaxb.JAXBEncoderDecoder.writeObject(JAXBEncoderDecoder.java:642)
        at org.apache.cxf.jaxb.JAXBEncoderDecoder.marshall(JAXBEncoderDecoder.java:244)
        at org.apache.cxf.jaxb.io.DataWriterImpl.write(DataWriterImpl.java:238)
        at org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor.writeParts(AbstractOutDatabindingInterceptor.java:118)
        at org.apache.cxf.wsdl.interceptors.BareOutInterceptor.handleMessage(BareOutInterceptor.java:68)
        at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:307)
        at org.apache.cxf.endpoint.ClientImpl.doInvoke(ClientImpl.java:528)
        at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:439)
        at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:354)
        at org.apache.cxf.endpoint.ClientImpl.invoke(ClientImpl.java:312)
        at org.apache.cxf.frontend.ClientProxy.invokeSync(ClientProxy.java:96)
        at org.apache.cxf.jaxws.JaxWsClientProxy.invoke(JaxWsClientProxy.java:140)
        ... 32 more
Caused by: java.io.IOException: Read end dead
        at java.base/java.io.PipedInputStream.checkStateForReceive(PipedInputStream.java:262)
        at java.base/java.io.PipedInputStream.awaitSpace(PipedInputStream.java:268)
        at java.base/java.io.PipedInputStream.receive(PipedInputStream.java:231)
        at java.base/java.io.PipedOutputStream.write(PipedOutputStream.java:150)
        at org.apache.cxf.io.AbstractWrappedOutputStream.write(AbstractWrappedOutputStream.java:51)
        at org.apache.cxf.io.AbstractThresholdOutputStream.write(AbstractThresholdOutputStream.java:69)
        at org.glassfish.jaxb.runtime.v2.runtime.output.UTF8XmlOutput.flushBuffer(UTF8XmlOutput.java:393)
        at org.glassfish.jaxb.runtime.v2.runtime.output.UTF8XmlOutput.text(UTF8XmlOutput.java:346)
        at org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.Base64Data.writeTo(Base64Data.java:299)
        at org.glassfish.jaxb.runtime.v2.runtime.output.UTF8XmlOutput.text(UTF8XmlOutput.java:286)
        at org.glassfish.jaxb.runtime.v2.runtime.XMLSerializer.leafElement(XMLSerializer.java:320)
        at org.glassfish.jaxb.runtime.v2.model.impl.RuntimeBuiltinLeafInfoImpl$PcdataImpl.writeLeafElement(RuntimeBuiltinLeafInfoImpl.java:151)
        at org.glassfish.jaxb.runtime.v2.runtime.reflect.TransducedAccessor$CompositeTransducedAccessorImpl.writeLeafElement(TransducedAccessor.java:224)
        at org.glassfish.jaxb.runtime.v2.runtime.property.SingleElementLeafProperty.serializeBody(SingleElementLeafProperty.java:94)
        at org.glassfish.jaxb.runtime.v2.runtime.ClassBeanInfoImpl.serializeBody(ClassBeanInfoImpl.java:334)
        at org.glassfish.jaxb.runtime.v2.runtime.XMLSerializer.childAsXsiType(XMLSerializer.java:659)
        at org.glassfish.jaxb.runtime.v2.runtime.property.SingleElementNodeProperty.serializeBody(SingleElementNodeProperty.java:128)
        at org.glassfish.jaxb.runtime.v2.runtime.ElementBeanInfoImpl$1.serializeBody(ElementBeanInfoImpl.java:125)
        at org.glassfish.jaxb.runtime.v2.runtime.ElementBeanInfoImpl$1.serializeBody(ElementBeanInfoImpl.java:93)
        at org.glassfish.jaxb.runtime.v2.runtime.ElementBeanInfoImpl.serializeBody(ElementBeanInfoImpl.java:316)
        at org.glassfish.jaxb.runtime.v2.runtime.ElementBeanInfoImpl.serializeRoot(ElementBeanInfoImpl.java:324)
        at org.glassfish.jaxb.runtime.v2.runtime.ElementBeanInfoImpl.serializeRoot(ElementBeanInfoImpl.java:38)
        at org.glassfish.jaxb.runtime.v2.runtime.XMLSerializer.childAsRoot(XMLSerializer.java:456)
        at org.glassfish.jaxb.runtime.v2.runtime.MarshallerImpl.write(MarshallerImpl.java:265)
        ... 46 more
```
