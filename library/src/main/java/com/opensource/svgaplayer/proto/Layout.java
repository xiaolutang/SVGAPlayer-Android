// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: svga.proto at 28:1
package com.opensource.svgaplayer.proto;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Float;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class Layout extends Message<Layout, Layout.Builder> {
  public static final ProtoAdapter<Layout> ADAPTER = new ProtoAdapter_Layout();

  private static final long serialVersionUID = 0L;

  public static final Float DEFAULT_X = 0.0f;

  public static final Float DEFAULT_Y = 0.0f;

  public static final Float DEFAULT_WIDTH = 0.0f;

  public static final Float DEFAULT_HEIGHT = 0.0f;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float x;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float y;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float width;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  public final Float height;

  public Layout(Float x, Float y, Float width, Float height) {
    this(x, y, width, height, ByteString.EMPTY);
  }

  public Layout(Float x, Float y, Float width, Float height, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.x = x;
    builder.y = y;
    builder.width = width;
    builder.height = height;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Layout)) return false;
    Layout o = (Layout) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(x, o.x)
        && Internal.equals(y, o.y)
        && Internal.equals(width, o.width)
        && Internal.equals(height, o.height);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (x != null ? x.hashCode() : 0);
      result = result * 37 + (y != null ? y.hashCode() : 0);
      result = result * 37 + (width != null ? width.hashCode() : 0);
      result = result * 37 + (height != null ? height.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (x != null) builder.append(", x=").append(x);
    if (y != null) builder.append(", y=").append(y);
    if (width != null) builder.append(", width=").append(width);
    if (height != null) builder.append(", height=").append(height);
    return builder.replace(0, 2, "Layout{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<Layout, Builder> {
    public Float x;

    public Float y;

    public Float width;

    public Float height;

    public Builder() {
    }

    public Builder x(Float x) {
      this.x = x;
      return this;
    }

    public Builder y(Float y) {
      this.y = y;
      return this;
    }

    public Builder width(Float width) {
      this.width = width;
      return this;
    }

    public Builder height(Float height) {
      this.height = height;
      return this;
    }

    @Override
    public Layout build() {
      return new Layout(x, y, width, height, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_Layout extends ProtoAdapter<Layout> {
    ProtoAdapter_Layout() {
      super(FieldEncoding.LENGTH_DELIMITED, Layout.class);
    }

    @Override
    public int encodedSize(Layout value) {
      return (value.x != null ? ProtoAdapter.FLOAT.encodedSizeWithTag(1, value.x) : 0)
          + (value.y != null ? ProtoAdapter.FLOAT.encodedSizeWithTag(2, value.y) : 0)
          + (value.width != null ? ProtoAdapter.FLOAT.encodedSizeWithTag(3, value.width) : 0)
          + (value.height != null ? ProtoAdapter.FLOAT.encodedSizeWithTag(4, value.height) : 0)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, Layout value) throws IOException {
      if (value.x != null) ProtoAdapter.FLOAT.encodeWithTag(writer, 1, value.x);
      if (value.y != null) ProtoAdapter.FLOAT.encodeWithTag(writer, 2, value.y);
      if (value.width != null) ProtoAdapter.FLOAT.encodeWithTag(writer, 3, value.width);
      if (value.height != null) ProtoAdapter.FLOAT.encodeWithTag(writer, 4, value.height);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public Layout decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.x(ProtoAdapter.FLOAT.decode(reader)); break;
          case 2: builder.y(ProtoAdapter.FLOAT.decode(reader)); break;
          case 3: builder.width(ProtoAdapter.FLOAT.decode(reader)); break;
          case 4: builder.height(ProtoAdapter.FLOAT.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public Layout redact(Layout value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
