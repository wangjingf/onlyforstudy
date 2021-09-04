package io.study.gateway.stat;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import io.study.gateway.registry.IRegistry;

public class MetricStreamChannelStats {
    IRegistry registry = null;
    String prefix;
    public MetricStreamChannelStats(String prefix,IRegistry registry){
        this.registry = registry;
        this.prefix = prefix;
        readMessageMeter = registry.newMeter(prefix+"read_message.meter");
        writeMessageMeter = registry.newMeter(prefix+"write_message.meter");
        readBytesMeter = registry.newMeter(prefix+"read_bytes.meter");
        writeBytesMeter = registry.newMeter(prefix+"write_bytes.meter");
        connectMeter = registry.newMeter(prefix+"connect.meter");
        connectionMeter = registry.newMeter(prefix+"connection.meter");
        writableFalseMeter = registry.newMeter(prefix+"writable_false.meter");
        exceptionMeter = registry.newMeter(prefix+"exception.meter");
        closeMeter = registry.newMeter(prefix+"close_meter.meter");
    }
     Meter readMessageMeter = null;
     Meter writeMessageMeter = null;
     Meter readBytesMeter = null;
     Meter writeBytesMeter = null;
     Meter connectMeter = null;
     Meter connectionMeter = null;
     Meter writableFalseMeter = null;
     Meter exceptionMeter = null;
     Meter closeMeter = null;

    public IRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(IRegistry registry) {
        this.registry = registry;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Meter getReadMessageMeter() {
        return readMessageMeter;
    }

    public void setReadMessageMeter(Meter readMessageMeter) {
        this.readMessageMeter = readMessageMeter;
    }

    public Meter getWriteMessageMeter() {
        return writeMessageMeter;
    }

    public void setWriteMessageMeter(Meter writeMessageMeter) {
        this.writeMessageMeter = writeMessageMeter;
    }

    public Meter getReadBytesMeter() {
        return readBytesMeter;
    }

    public void setReadBytesMeter(Meter readBytesMeter) {
        this.readBytesMeter = readBytesMeter;
    }

    public Meter getWriteBytesMeter() {
        return writeBytesMeter;
    }

    public void setWriteBytesMeter(Meter writeBytesMeter) {
        this.writeBytesMeter = writeBytesMeter;
    }

    public Meter getConnectMeter() {
        return connectMeter;
    }

    public void setConnectMeter(Meter connectMeter) {
        this.connectMeter = connectMeter;
    }

    public Meter getConnectionMeter() {
        return connectionMeter;
    }

    public void setConnectionMeter(Meter connectionMeter) {
        this.connectionMeter = connectionMeter;
    }

    public Meter getWritableFalseMeter() {
        return writableFalseMeter;
    }

    public void setWritableFalseMeter(Meter writableFalseMeter) {
        this.writableFalseMeter = writableFalseMeter;
    }

    public Meter getExceptionMeter() {
        return exceptionMeter;
    }

    public void setExceptionMeter(Meter exceptionMeter) {
        this.exceptionMeter = exceptionMeter;
    }

    public Meter getCloseMeter() {
        return closeMeter;
    }

    public void setCloseMeter(Meter closeMeter) {
        this.closeMeter = closeMeter;
    }
}
