package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class UseNio {
    /**
     * 注意 buf.flip() 的调用，首先读取数据到Buffer，然后反转Buffer,接着再从Buffer中读取数据
     * <p>
     * 使用Buffer读写数据一般遵循以下四个步骤：
     * 写入数据到Buffer
     * 调用flip()方法
     * 从Buffer中读取数据
     * 调用clear()方法或者compact()方法
     * <p>
     * 当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据。
     * 一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
     */

    private static void useNio() {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("SerialVersion.txt", "rw");
            FileChannel inChannel = aFile.getChannel();

            //创建容量为48byte的buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            int byteReader = inChannel.read(byteBuffer);    //读取数据，放入buffer

            while (byteReader != -1) {
                System.out.println("Read:" + byteReader);
                byteBuffer.flip();  //设置buffer切换模式为读模式

                while (byteBuffer.hasRemaining()) {
                    System.out.println((char) byteBuffer.get()); // 每次读取1byte，也就是一个字节
                }

                byteBuffer.clear(); //清空buffer，准备再次写入
                byteReader = inChannel.read(byteBuffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                aFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void transferFrom() throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);   //将数据从源通道传输到FileChannel中
//        fromChannel.transferTo(position, count, toChannel);   //方法将数据从FileChannel传输到其他的channel中
    }

    private static void selector() throws Exception {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("https://cdn.bootcss.com/jquery/3.3.1/jquery.js", 80));
        channel.configureBlocking(false);   //false非阻塞模式下
        SelectionKey keys = channel.register(selector, SelectionKey.OP_READ);   //interest集合 读就绪

        while (true) {
            int readyChannels = selector.select();  //阻塞到至少有一个通道在你注册的事件上就绪了。
            if (readyChannels == 0) continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();       //就绪通道
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            //这个循环遍历已选择键集中的每个键，并检测各个键所对应的通道的就绪事件
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    System.out.println("一个连接被ServerSocketChannel接受");
                } else if (key.isConnectable()) {
                    System.out.println("与远程服务器建立了连接");
                } else if (key.isReadable()) {
                    System.out.println("一个channel做好了读准备");
                } else if (key.isWritable()) {
                    System.out.println("一个channel做好了写准备");
                }
                keyIterator.remove();   //keyIterator.remove()调用。Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除
            }
        }
    }

    private static void fileChannel() throws Exception{
        RandomAccessFile aFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);
        System.out.println("bytesRead " + bytesRead);
        System.out.println("1fileSize " + inChannel.size());


        String newData = "\nNew String to write to file..." + System.currentTimeMillis();

        ByteBuffer wbuf = ByteBuffer.allocate(48);
        wbuf.clear();
        wbuf.put(newData.getBytes());

        wbuf.flip();

        while(wbuf.hasRemaining()) {
            inChannel.write(wbuf);
        }

        System.out.println("2fileSize " + inChannel.size());

//        System.out.println("truncate " + inChannel.truncate(30));

        inChannel.close();
    }



    public static void main (String args[]) throws Exception {
//            useNio();
//            transferFrom();
//            selector();
        fileChannel();
        }

    }
