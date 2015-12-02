

import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.util.zip.GZIPInputStream;
 import java.util.zip.GZIPOutputStream;
 

 public class Gzip {
 
  
     /**
      * �ַ��ѹ��
      * 
      * @param str
      *            ��ѹ�����ַ�
      * @return    ����ѹ������ַ�
      * @throws IOException
      */
     public static String compress(String str) throws IOException {
         if (null == str || str.length() <= 0) {
             return str;
         }
         // ����һ���µ� byte ���������
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         // ʹ��Ĭ�ϻ������С�����µ������
         GZIPOutputStream gzip = new GZIPOutputStream(out);
         // �� b.length ���ֽ�д��������
         gzip.write(str.getBytes());
         gzip.close();
         // ʹ��ָ���� charsetName��ͨ������ֽڽ�����������ת��Ϊ�ַ�
         return out.toString("ISO-8859-1");
     }
     
     /**
      * �ַ�Ľ�ѹ
      * 
      * @param str
      *            ���ַ��ѹ
      * @return    ���ؽ�ѹ������ַ�
      * @throws IOException
      */
     public static String unCompress(String str) throws IOException {
         if (null == str || str.length() <= 0) {
             return str;
         }
         // ����һ���µ� byte ���������
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         // ����һ�� ByteArrayInputStream��ʹ�� buf ��Ϊ�仺��������
         ByteArrayInputStream in = new ByteArrayInputStream(str
                 .getBytes("ISO-8859-1"));
         // ʹ��Ĭ�ϻ������С�����µ�������
         GZIPInputStream gzip = new GZIPInputStream(in);
         byte[] buffer = new byte[256];
         int n = 0;
         while ((n = gzip.read(buffer)) >= 0) {// ��δѹ����ݶ����ֽ�����
             // ��ָ�� byte �����д�ƫ���� off ��ʼ�� len ���ֽ�д��� byte���������
             out.write(buffer, 0, n);
         }
         // ʹ��ָ���� charsetName��ͨ������ֽڽ�����������ת��Ϊ�ַ�
         return out.toString("GBK");
     }
 
 }

