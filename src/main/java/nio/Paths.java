package nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Paths {
    public static void main(String[] args) throws Exception {
//        pathTest();
        fileTest();
    }

    public static void pathTest() {
        Path path = java.nio.file.Paths.get("path.txt");
        System.out.println(path.toAbsolutePath());

        path.normalize();   //可以使路径标准化。标准化意味着它将移除所有在路径字符串的中间的.和..代码
        System.out.println(path.toAbsolutePath());
        System.out.println(path.getFileSystem());
        System.out.println(path.getNameCount());
        System.out.println(path.getParent());
        System.out.println(path.getRoot());
        System.out.println(path.getFileName());
        System.out.println(path.getName(0));
    }

    public static void fileTest() throws Exception {
        //Files.exists()
        Path path = java.nio.file.Paths.get("SerialVersion.txt");
        //第二个参数。这个参数是一个选项数组，它影响Files.exists()如何确定路径是否存在
        //LinkOption.NOFOLLOW_LINKS Files.exists()方法不应该在文件系统中跟踪符号链接，以确定文件是否存在
        boolean pathExists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        System.out.println(pathExists);

        //Files.createDirectory()
        Path path2 = java.nio.file.Paths.get("filedir");
        try {
            Path newDir = Files.createDirectory(path2);
        } catch (FileAlreadyExistsException e) {
            // 目录已经存在
        } catch (IOException e) {
            // 其他发生的异常
            e.printStackTrace();
        }
        System.out.println(path2.toAbsolutePath());

        //Files.copy()
        Path sourcePath = java.nio.file.Paths.get("SerialVersion.txt");
        Path destinationPath = java.nio.file.Paths.get("filedir/SerialVersion-copy.txt");
        System.out.println(destinationPath.toAbsolutePath());
        try {
//            Files.copy(sourcePath, destinationPath);
            //重写已存在的文件
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            // 目录已经存在
        } catch (IOException e) {
            // 其他发生的异常
            e.printStackTrace();
        }

//        Files.move()
//        Files.delete();

//        Files.walkFileTree() 递归遍历目录树 扩展SimpleFileVisitor类，它包含FileVisitor接口中所有方法的默认实现。
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            //这四个方法中的每个都返回一个FileVisitResult枚举实例。FileVisitResult枚举包含以下四个选项:
            //            CONTINUE 继续
            //            TERMINATE 终止
            //            SKIP_SIBLING 跳过同级
            //            SKIP_SUBTREE 跳过子级

            //在访问任何目录之前调用
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("pre visit dir:" + dir);
                return FileVisitResult.CONTINUE;
            }

            //文件遍历过程中访问的每一个文件。它不会访问目录-只会访问文件
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("visit file: " + file);
                return FileVisitResult.CONTINUE;
            }

            //访问文件失败时调用。例如，如果您没有正确的权限，或者其他什么地方出错了。
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("visit file failed: " + file);
                return FileVisitResult.CONTINUE;
            }

            //问一个目录之后调用
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("post visit directory: " + dir);
                return FileVisitResult.CONTINUE;
            }
        });

        //文件搜索
        Path rootPath = java.nio.file.Paths.get("filedir");
        String fileToFind = File.separator + "SerialVersion-copy.txt";
        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileString = file.toAbsolutePath().toString();
                    System.out.println("pathString = " + fileString);

                    if (fileString.endsWith(fileToFind)) {
                        System.out.println("file found at path: " + file.toAbsolutePath());
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        //递归删除目录
        Path delPath = java.nio.file.Paths.get("to-delete");
        if(!Files.exists(delPath)){
            Files.createDirectory(delPath);
        }

        for (int i=0;i<10;i++){
            String newFile = "file" + i;
            Path newPath = java.nio.file.Paths.get(delPath + File.separator + newFile);
            boolean fileExists = Files.exists(newPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
            if(!fileExists) {
                Files.createFile(newPath);
                System.out.println("file create : " + newPath.getFileName());
            }
        }

        try {
            Files.walkFileTree(delPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("delete file: " + file.toString());
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    System.out.println("delete dir: " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch(IOException e){
            e.printStackTrace();
        }

    }
}
