package file.storage_system.util;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class FileUtil {

    public static byte[] compressFile(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] tmpBytes = new byte[4*1824];
        while (!deflater.finished()){
            int size = deflater.deflate(tmpBytes);
            byteArrayOutputStream.write(tmpBytes, 0, size);
        }
        try {
            byteArrayOutputStream.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }
    public static byte[] decompressFile(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] tmpBytes = new byte[4*1824];
        try{
            while (!inflater.finished()){
                int count = inflater.inflate(tmpBytes);
                byteArrayOutputStream.write(tmpBytes, 0, count);
            }
            byteArrayOutputStream.close();

        } catch (DataFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
