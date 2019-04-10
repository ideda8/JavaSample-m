package nio;


import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

public class AsynchronousFileChannel {
    public static void main(String[] args) throws Exception {
//        asyncFileChannelFuture();
//        asyncFileChannelCompletionHandler();

        asyncFileChannelFutureWrite();
        asyncFileChannelCompletionHandlerWrite();
    }

    public static void asyncFileChannelFuture() throws Exception {
        Path path = java.nio.file.Paths.get("SerialVersion.txt");
        java.nio.channels.AsynchronousFileChannel fileChannel
                = java.nio.channels.AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long position = 0;

        //传递给read()方法作为参数，以及一个0的位置
        Future<Integer> operation = fileChannel.read(buffer, position);

        while (!operation.isDone()) ;   //循环，直到返回的isDone()方法返回true

        //读取操作完成后，数据读取到ByteBuffer中，然后进入一个字符串并打印到System.out中
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        System.out.println(new String(data));
        buffer.clear();
    }

    public static void asyncFileChannelCompletionHandler() throws Exception {
        Path path = java.nio.file.Paths.get("asyncCH.txt");
        java.nio.channels.AsynchronousFileChannel fileChannel
                = java.nio.channels.AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long position = 0;

        fileChannel.read(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            //一旦读取操作完成调用
            //传递一个整数，它告诉我们读取了多少字节，以及传递给read()方法的“附件”
            //“附件”是read()方法的第三个参数。在本例中，它是ByteBuffer，数据也被读取。您可以自由选择要附加的对象。
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("result = " + result);

                attachment.flip();
                byte[] data = new byte[attachment.limit()];
                attachment.get(data);
                System.out.println(new String(data));
                attachment.clear();
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }

    public static void asyncFileChannelFutureWrite() throws Exception {
        Path path = java.nio.file.Paths.get("asyncFuture.txt");
        if(!Files.exists(path)){
            Files.createFile(path);
        }

        String text = "test data";
        System.out.println(text.getBytes().length);

        java.nio.channels.AsynchronousFileChannel fileChannel
                = java.nio.channels.AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(text.getBytes().length);
        long position = 0;

        buffer.put(text.getBytes());
        buffer.flip();

        Future<Integer> operation = fileChannel.write(buffer, position);
        buffer.clear();

        while (!operation.isDone()) ;   //循环，直到返回的isDone()方法返回true

        System.out.println("Write done");
    }

    public static void asyncFileChannelCompletionHandlerWrite() throws Exception {
        Path path = java.nio.file.Paths.get("asyncCH.txt");
        if(!Files.exists(path)){
            Files.createFile(path);
        }

        String text = "test data";
        System.out.println(text.getBytes().length);

        java.nio.channels.AsynchronousFileChannel fileChannel
                = java.nio.channels.AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(text.getBytes().length);
        long position = 0;

        buffer.put(text.getBytes());
        buffer.flip();

        fileChannel.write(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            //当写操作完成时调用
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("bytes written: " + result);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("Write failed");
                exc.printStackTrace();
            }
        });
    }

}
