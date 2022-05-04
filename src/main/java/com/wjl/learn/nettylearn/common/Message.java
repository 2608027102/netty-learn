package com.wjl.learn.nettylearn.common;

import com.wjl.learn.nettylearn.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public abstract class Message<T extends MessageBody> {

    private MessageHeader messageHeader;
    private T messageBody;

    public void encode(ByteBuf buf) {
        buf.writeInt(messageHeader.getVersion());
        buf.writeLong(messageHeader.getStreamId());
        buf.writeInt(messageHeader.getOpCode());
        buf.writeBytes(JsonUtil.toJsonString(messageBody).getBytes(StandardCharsets.UTF_8));
    }

    public abstract Class<? extends T> getMessageBodyDecodeClass(int opcode);

    public void decode(ByteBuf buf) {
        int version = buf.readInt();
        long streamId = buf.readLong();
        int opCode = buf.readInt();

        MessageHeader header = new MessageHeader();
        header.setVersion(version);
        header.setStreamId(streamId);
        header.setOpCode(opCode);

        Class<? extends T> messageBodyDecodeClass = getMessageBodyDecodeClass(opCode);
        T body = JsonUtil.parse(buf.toString(StandardCharsets.UTF_8), messageBodyDecodeClass);

        this.messageHeader = header;
        this.messageBody = body;
    }

}
