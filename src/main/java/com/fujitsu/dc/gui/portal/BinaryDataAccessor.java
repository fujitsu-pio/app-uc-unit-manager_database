/**
 * Personium
 * Copyright 2016 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fujitsu.dc.gui.portal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ファイルシステムに対してWebDAVのバイナリファイルの入出力を行うアクセサクラス.
 */
public class BinaryDataAccessor {

    private static final int FILE_BUFFER_SIZE = 1024;
    private String baseDir;

    /**
     * コンストラクタ.
     * @param path 格納ディレクトリ
     */
    public BinaryDataAccessor(String path) {
        this.baseDir = path;
        if (!this.baseDir.endsWith("/")) {
            this.baseDir += "/";
        }
    }

    /**
     * ストリームから読み込んだデータをファイルに書き込む.
     * @param inputStream 入力元のストリーム
     * @param filename ファイル名
     * @throws BinaryDataAccessException ファイル出力で異常が発生した場合にスローする
     */
    public void create(InputStream inputStream, String filename) throws BinaryDataAccessException {
        String directory = getSubDirectoryName(filename);
        String fullPathName = this.baseDir + directory + filename;
      //  createSubDirectories(this.baseDir + directory);
        writeToFile(inputStream, fullPathName);
    }

    /**
     * ストリームから読み込んだデータをファイルに書き込む.
     * @param inputStream 入力元のストリーム
     * @param filename ファイル名
     * @throws BinaryDataAccessException ファイル入出力で異常が発生した場合にスローする
     */
    public void update(InputStream inputStream, String filename) throws BinaryDataAccessException {
        String directory = getSubDirectoryName(filename);
        String fullPathName = this.baseDir + directory + filename;
        if (!exists(fullPathName)) {
            throw new BinaryDataNotFoundException(fullPathName);
        }
        writeToFile(inputStream, fullPathName);
    }

    /**
     * ファイルをストリームにコピーする.
     * @param filename ファイル名
     * @param outputStream コピー先ストリーム
     * @throws BinaryDataAccessException ファイル入出力で異常が発生した場合にスローする
     */
    public void copy(String filename, OutputStream outputStream) throws BinaryDataAccessException {
        String directory = getSubDirectoryName(filename);
        String fullPathName = this.baseDir + directory + filename;
        if (!exists(fullPathName)) {
            throw new BinaryDataNotFoundException(fullPathName);
        }
        writeToStream(fullPathName, outputStream);
    }

    /**
     * ファイルを論理削除する.
     * 対象ファイルが存在しない場合は何もしない
     * @param filename ファイル名
     * @throws BinaryDataAccessException ファイル入出力で異常が発生した場合にスローする
     */
    /*public void delete(String filename) throws BinaryDataAccessException {
        String directory = getSubDirectoryName(filename);
        String fullPathName = this.baseDir + directory + filename;
        if (exists(fullPathName)) {
           // deleteFile(fullPathName);
        }
    }*/

    /**
     * ファイルサイズを返す.
     * @param filename ファイル名
     * @return ファイルサイズ(bytes)
     */
    public long getSize(String filename) {
        String directory = getSubDirectoryName(filename);
        String fullPathName = this.baseDir + directory + filename;
        return getFileSize(fullPathName);
    }

    private static final int SUBDIR_NAME_LEN = 2;

    private boolean exists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    private String getSubDirectoryName(String filename) {
        StringBuilder sb = new StringBuilder("");
        sb.append(splitDirectoryName(filename, 0));
        sb.append("/");
        sb.append(splitDirectoryName(filename, SUBDIR_NAME_LEN));
        sb.append("/");
        return sb.toString();
    }

    private String splitDirectoryName(String filename, int index) {
        return filename.substring(index, index + SUBDIR_NAME_LEN);
    }

 //   private void createSubDirectories(String directory) throws BinaryDataAccessException {
      /*  File newDir = new File(directory);
        // 既にディレクトリがあれば、何もしない
        if (!newDir.exists()) {
            try {
                Files.createDirectories(newDir.topath());
            } catch (IOException e) {
                throw new BinaryDataAccessException("DirectoryCreateFailed:" + directory, e);
            }
        }*/
  //  }

    private void writeToFile(InputStream inputStream, String fullPathName) throws BinaryDataAccessException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fullPathName);
            copyStream(inputStream, outputStream);
        } catch (IOException ex) {
            throw new BinaryDataAccessException("WriteToFileFailed:" + fullPathName, ex);
        } finally {
            closeOutputStream(outputStream);
        }
    }

    private void writeToStream(String fullPathName, OutputStream outputStream) throws BinaryDataAccessException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fullPathName);
            copyStream(inputStream, outputStream);
        } catch (IOException ex) {
            throw new BinaryDataAccessException("WriteToStreamFailed:" + fullPathName, ex);
        } finally {
            closeInputStream(inputStream);
        }
    }

    private void copyStream(InputStream inputStream, OutputStream outputStream) throws BinaryDataAccessException {
        BufferedInputStream bufferedInput = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            bufferedInput = new BufferedInputStream(inputStream);
            bufferedOutput = new BufferedOutputStream(outputStream);
            byte[] buf = new byte[FILE_BUFFER_SIZE];
            int len;
            while ((len = bufferedInput.read(buf)) != -1) {
                bufferedOutput.write(buf, 0, len);
            }
        } catch (IOException ex) {
            throw new BinaryDataAccessException("CopyStreamFailed.", ex);
        } finally {
            closeOutputStream(bufferedOutput);
            closeInputStream(bufferedInput);
        }
    }

    private void closeInputStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            String[] logDetails = new String[]{ex.toString()};
            PropertiesUtil.appendLog("OT-ER-002", logDetails, false);
        }
    }

    private void closeOutputStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException ex) {
            String[] logDetails = new String[]{ex.toString()};
            PropertiesUtil.appendLog("OT-ER-002", logDetails, false);
        }
    }
    //TODO : Uncomment it.
  //  private void deleteFile(String srcFullPathName) throws BinaryDataAccessException {
       /* String dstFullPathName = srcFullPathName + ".deleted";
        File srcFile = new File(srcFullPathName);
        File dstFile = new File(dstFullPathName);
        try {
            if (dstFile.exists()) {
                Files.delete(dstFile.toPath());
            }
            //Files.move(srcFile.toPath(), dstFile.toPath());
        } catch (IOException e) {
            throw new BinaryDataAccessException("DeleteFailed:" + srcFullPathName, e);
        }*/
  //  }

    private long getFileSize(String fullPathName) {
        File file = new File(fullPathName);
        return file.length();
    }
}
