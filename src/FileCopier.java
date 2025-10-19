import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileCopier {
    private static void copyFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static boolean copyFile(String sourcePath, String destPath) {
        try {
            File sourceFile = new File(sourcePath);
            File destFile = new File(destPath);

            File destDir = destFile.getParentFile();
            if (destDir != null && !destDir.exists()) {
                destDir.mkdirs();
            }

            copyFile(sourceFile, destFile);
            System.out.println("Файл успешно скопирован:");
            System.out.println("Из: " + sourceFile.getAbsolutePath());
            System.out.println("В: " + destFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка при копировании файла: " + e.getMessage());
            return false;
        }
    }

    public static void CopyTwoFiles(String sourcePath1, String destPath1, String sourcePath2, String destPath2) {
        System.out.println("Последовательное копирование двух файлов ");
        long startTime = System.currentTimeMillis();
        boolean firstCopySuccess = false;
        boolean secondCopySuccess = false;

        try {
            File destFile1 = new File(destPath1);
            File destDir1 = destFile1.getParentFile();
            if (destDir1 != null && !destDir1.exists()) {
                destDir1.mkdirs();
            }
            File sourceFile1 = new File(sourcePath1);
            if (!sourceFile1.exists()) {
                System.out.println("Файл не существует " + sourcePath1);
            } else {
                copyFile(sourceFile1, destFile1);
                firstCopySuccess = true;
                System.out.println("Первый файл успешно скопирован:");
                System.out.println("Из: " + sourceFile1.getAbsolutePath());
                System.out.println("В: " + destFile1.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при копировании первого файла: " + e.getMessage());
        }

        try {
            File destFile2 = new File(destPath2);
            File destDir2 = destFile2.getParentFile();
            if (destDir2 != null && !destDir2.exists()) {
                destDir2.mkdirs();
            }
            File sourceFile2 = new File(sourcePath2);
            if (!sourceFile2.exists()) {
                System.out.println("Файл не существует " + sourceFile2);
            } else {
                copyFile(sourceFile2, destFile2);
                secondCopySuccess = true;
                System.out.println("Второй файл успешно скопирован:");
                System.out.println("Из: " + sourceFile2.getAbsolutePath());
                System.out.println("В: " + destFile2.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при копировании второго файла: " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Общее время выполнения: " + duration + " мс");
    }
    public static void paralCopyTwoFiles(String sourcePath1, String destPath1, String sourcePath2, String destPath2) {
        System.out.println("Параллельное копирование двух файлов ");
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Boolean> future1 = executor.submit(() -> {
            try {
                File destFile1 = new File(destPath1);
                File destDir1 = destFile1.getParentFile();
                if (destDir1 != null && !destDir1.exists()) {
                    destDir1.mkdirs();
                }
                File sourceFile1 = new File(sourcePath1);
                if (!sourceFile1.exists()) {
                    System.out.println("Файл не существует ");
                    return false;
                } else {
                    copyFile(sourceFile1, destFile1);
                    System.out.println("Первый файл успешно скопирован (поток: " +
                            Thread.currentThread().getName() + "):");
                    System.out.println("Из: " + sourceFile1.getAbsolutePath());
                    System.out.println("В: " + destFile1.getAbsolutePath());
                    return true;
                }
            } catch (IOException e) {
                System.out.println("Ошибка при копировании первого файла: " + e.getMessage());
                return false;
            }
        });
        Future<Boolean> future2 = executor.submit(() -> {
            try {
                File destFile2 = new File(destPath2);
                File destDir2 = destFile2.getParentFile();
                if (destDir2 != null && !destDir2.exists()) {
                    destDir2.mkdirs();
                }
                File sourceFile2 = new File(sourcePath2);
                if (!sourceFile2.exists()) {
                    System.out.println("Файл не существует ");
                    return false;
                } else {
                    copyFile(sourceFile2, destFile2);
                    System.out.println("торой файл успешно скопирован (поток: " +
                            Thread.currentThread().getName() + "):");
                    System.out.println("Из: " + sourceFile2.getAbsolutePath());
                    System.out.println("В: " + destFile2.getAbsolutePath());
                    return true;
                }
            } catch (IOException e) {
                System.out.println("Ошибка при копировании второго файла: " + e.getMessage());
                return false;
            }
        });
        boolean firstCopySuccess = false;
        boolean secondCopySuccess = false;

        try {
            firstCopySuccess = future1.get();
            secondCopySuccess = future2.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ошибка при выполнении параллельного копирования: " + e.getMessage());
        }
            executor.shutdown();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Общее время выполнения: " + duration + " мс");

    }
    public class Main {
        public static void main(String[] args) {
            String sourceFile1 = "C:\\Users\\PC\\Downloads\\labCopy1.txt";
            String destFile1 = "backup/labCopy1.txt";

            String sourceFile2 = "C:\\Users\\PC\\Downloads\\labCopy2.txt";
            String destFile2 = "backup/labCopy2.txt";
            System.out.println("Последовательное копирование двух файлов");
            FileCopier.CopyTwoFiles(sourceFile1, destFile1, sourceFile2, destFile2);

            System.out.println(" Копирование одного файла");
            String sourceFile3 = "C:\\Users\\PC\\Downloads\\labCopy.txt";
            String destFile3 = "backup/labCopy.txt";
            FileCopier.copyFile(sourceFile3, destFile3);

            System.out.println("Паралледбное копирование двух файлов");
            FileCopier.paralCopyTwoFiles(sourceFile1, destFile1,sourceFile2,destFile2);

            long startTime = System.currentTimeMillis();
            boolean success = FileCopier.copyFile(sourceFile3, destFile3);
            long endTime = System.currentTimeMillis();
            if (success) {
                System.out.println("Время копирования одного файла: " + (endTime - startTime) + " мс");
            }
        }
    }
}