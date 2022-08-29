package com.thyrocare.btechapp.utils.fileutils.fileupload;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class UploadMultipartEntity extends MultipartEntity {

    private final ProgressListener listener;

    public UploadMultipartEntity(final ProgressListener listener) {
        super();
        this.listener = listener;
    }

    public UploadMultipartEntity(final HttpMultipartMode mode,
                                 final ProgressListener listener) {
        super(mode);
        this.listener = listener;
    }

    public UploadMultipartEntity(HttpMultipartMode mode, final String boundary,
                                 final Charset charset, final ProgressListener listener) {
        super(mode, boundary, charset);
        this.listener = listener;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, this.listener));
    }

    public interface ProgressListener {
        void transferred(long num);
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out,
                                    final ProgressListener listener) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {

            android.os.Process.setThreadPriority(12);

            int length = len;

            int offset = off;

            int buffer = 20480;// buffer size, I made it 20KB, 1024*20

            int remainer = len;

            while (length > offset) {

                if (remainer < buffer)

                    buffer = remainer;

                out.write(b, offset, buffer);

                remainer -= buffer;

                offset += buffer;

                this.transferred += buffer;

                this.listener.transferred(this.transferred);

                // Logger.debug("Size of file transferred :" +
                // this.transferred);

                // out.flush();

            }
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred);
        }
    }
}