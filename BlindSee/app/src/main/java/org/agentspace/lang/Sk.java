package org.agentspace.lang;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Sk {

  static final byte[] win2usa = {
          (byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,  (byte)0x08,(byte)0x09,(byte)0x0a,(byte)0x0b,(byte)0x0c,(byte)0x0d,(byte)0x0e,(byte)0x0f,
          (byte)0x10,(byte)0x11,(byte)0x12,(byte)0x13,(byte)0x14,(byte)0x15,(byte)0x16,(byte)0x17,  (byte)0x18,(byte)0x19,(byte)0x1a,(byte)0x1b,(byte)0x1c,(byte)0x1d,(byte)0x1e,(byte)0x1f,
          (byte)0x20,(byte)0x21,(byte)0x22,(byte)0x23,(byte)0x24,(byte)0x25,(byte)0x26,(byte)0x27,  (byte)0x28,(byte)0x29,(byte)0x2a,(byte)0x2b,(byte)0x2c,(byte)0x2d,(byte)0x2e,(byte)0x2f,
          (byte)0x30,(byte)0x31,(byte)0x32,(byte)0x33,(byte)0x34,(byte)0x35,(byte)0x36,(byte)0x37,  (byte)0x38,(byte)0x39,(byte)0x3a,(byte)0x3b,(byte)0x3c,(byte)0x3d,(byte)0x3e,(byte)0x3f,

          (byte)0x40,(byte)0x41,(byte)0x42,(byte)0x43,(byte)0x44,(byte)0x45,(byte)0x46,(byte)0x47,  (byte)0x48,(byte)0x49,(byte)0x4a,(byte)0x4b,(byte)0x4c,(byte)0x4d,(byte)0x4e,(byte)0x4f,
          (byte)0x50,(byte)0x51,(byte)0x52,(byte)0x53,(byte)0x54,(byte)0x55,(byte)0x56,(byte)0x57,  (byte)0x58,(byte)0x59,(byte)0x5a,(byte)0x5b,(byte)0x5c,(byte)0x5d,(byte)0x5e,(byte)0x5f,
          (byte)0x60,(byte)0x61,(byte)0x62,(byte)0x63,(byte)0x64,(byte)0x65,(byte)0x66,(byte)0x67,  (byte)0x68,(byte)0x69,(byte)0x6a,(byte)0x6b,(byte)0x6c,(byte)0x6d,(byte)0x6e,(byte)0x6f,
          (byte)0x70,(byte)0x71,(byte)0x72,(byte)0x73,(byte)0x74,(byte)0x75,(byte)0x76,(byte)0x77,  (byte)0x78,(byte)0x79,(byte)0x7a,(byte)0x7b,(byte)0x7c,(byte)0x7d,(byte)0x7e,(byte)0x7f,

          /*    0128       0129       0130       0131       0132       0133       0134       0135         0136       0137       0138       0139       0140       0141       0142       0143 */
          (byte)0x80,(byte)0x81,(byte)0x82,(byte)0x83,(byte)0x84,(byte)0x85,(byte)0x86,(byte)0x87,  (byte)0x88,(byte)0x89,(byte)0x8a,(byte)0x8b,(byte)0x8c,(byte)0x8d,(byte)0x8e,(byte)0x8f,
          /*    0144       0145       0146       0147       0148       0149       0150       0151         0152       0153       0154       0155       0156       0157       0158       0159 */
          (byte)0x90,(byte)0x91,(byte)0x92,(byte)0x93,(byte)0x94,(byte)0x95,(byte)0x96,(byte)0x97,  (byte)0x98,(byte)0x99,(byte)0x9a,(byte)0x9b,(byte)0x9c,(byte)0x9d,(byte)0x9e,(byte)0x9f,
          /*    0160       0161       0162       0163       0164       0165       0166       0167         0168       0169       0170       0171       0172       0173       0174       0175 */
          (byte)0xa0,(byte)0xa1,(byte)0xa2,(byte)0xa3,(byte)0xa4,(byte)0x4c,(byte)0xa6,(byte)0xa7,  (byte)0xa8,(byte)0x53,(byte)0xaa,(byte)0x54,(byte)0xac,(byte)0xad,(byte)0x5a,(byte)0xaf,
          /*    0176       0177       0178       0179       0180       0181       0182       0183         0184       0185       0186       0187       0188       0189       0190       0191 */
          (byte)0xb0,(byte)0xb1,(byte)0xb2,(byte)0xb3,(byte)0xb4,(byte)0x6c,(byte)0x73,(byte)0xb7,  (byte)0xb8,(byte)0x73,(byte)0xba,(byte)0x74,(byte)0xbc,(byte)0xbd,(byte)0x7a,(byte)0xbf,

          /*    0192       0193       0194       0195       0196       0197       0198       0199         0200       0201       0202       0203       0204       0205       0206       0207 */
          (byte)0x52,(byte)0x41,(byte)0xc2,(byte)0xc3,(byte)0xc4,(byte)0x4c,(byte)0xc6,(byte)0xc7,  (byte)0x43,(byte)0x45,(byte)0xca,(byte)0xcb,(byte)0xcc,(byte)0x49,(byte)0xce,(byte)0x44,
          /*    0208       0209       0210       0211       0212       0213       0214       0215         0216       0217       0218       0219       0220       0221       0222       0223 */
          (byte)0xd0,(byte)0xd1,(byte)0x4e,(byte)0x4f,(byte)0xd4,(byte)0xd5,(byte)0xd6,(byte)0xd7,  (byte)0xd8,(byte)0xd9,(byte)0x55,(byte)0xdb,(byte)0xdc,(byte)0x59,(byte)0xde,(byte)0xdf,
          /*    0224       0225       0226       0227       0228       0229       0230       0231         0232       0233       0234       0235       0236       0237       0238       0239 */
          (byte)0x72,(byte)0x61,(byte)0xe2,(byte)0xe3,(byte)0x61,(byte)0x6c,(byte)0xe6,(byte)0xe7,  (byte)0x63,(byte)0x65,(byte)0xea,(byte)0xeb,(byte)0xec,(byte)0x69,(byte)0xee,(byte)0x64,
          /*    0240       0241       0242       0243       0244       0245       0246       0247         0248       0249       0250       0251       0252       0253       0254       0255 */
          (byte)0xf0,(byte)0xf1,(byte)0x6e,(byte)0x6f,(byte)0x6f,(byte)0xf5,(byte)0xf6,(byte)0xf7,  (byte)0xf8,(byte)0xf9,(byte)0x75,(byte)0xfb,(byte)0xfc,(byte)0x79,(byte)0xfe,(byte)0xff
  };

  public static String utf82usa (String str) {
    byte[] encoded;
    try {
      encoded = str.getBytes("ISO-8859-2");
    }
    catch (UnsupportedEncodingException ee) {
      encoded = str.getBytes();
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < encoded.length; i++) {
      int ch = encoded[i];
      if (ch < 0) ch = 256 + ch;
      if (ch == '\r' || ch == '\n') break;
      ch = win2usa[ch];
      if (ch < 0) ch = 256 + ch;
      sb.append((char) ch);
    }
    return sb.toString();
  }
 
}

