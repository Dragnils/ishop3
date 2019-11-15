package net.devstudy.ishop.filter;

//Написать фильтр, который автоматически удалить все символы из Response: \t \r \n
// проверять по http://localhost:8080/ishop_war/trim  в сравнении с original.jsp
// http://localhost:8080/ishop_war/trim-params.html в сравнении с parms.html

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

@WebFilter({"/trim", "/trim-params.html"})
public class TrimResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        TrimResponse response = new TrimResponse((HttpServletResponse) resp);
        chain.doFilter(req, response);
        response.complete();
    }

    @Override
    public void destroy() {
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class TrimResponse extends HttpServletResponseWrapper {
        private TrimProxyWriter trimProxyWriter;
        private TrimResponse(HttpServletResponse response) throws IOException {
            super(response);
            trimProxyWriter = new TrimProxyWriter(response.getWriter()); // создаем наш оригинальный райтер, с оригинального response получив getWriter
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(trimProxyWriter);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new ServletOutputStream(){
                @Override
                public boolean isReady() {
                    return false;
                }
                @Override
                public void setWriteListener(WriteListener writeListener) {
                }
                @Override
                public void write(int b) throws IOException {
                    trimProxyWriter.write(b);
                }
            };
        }

        private void complete() throws IOException {
            setContentLength(trimProxyWriter.getLength());
            trimProxyWriter.complete();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class TrimProxyWriter extends Writer { // класс для очищения ненужных символов
        private final Writer wr; // оригинальный райтер который сразу же записывается в наш response
        private int length;
        private TrimProxyWriter(Writer wr) {
            super();
            this.wr = wr;
            this.length = 0;
        }
        @Override
        public void write(int c) throws IOException {
            processChar((char)c);
        }
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            for (int i = off; i < len; i++) {
                processChar(cbuf[i]);
            }
        }
        @Override
        public void write(String str, int off, int len) throws IOException {
            for (int i = off; i < len; i++) {
                processChar(str.charAt(i));
            }
        }
        private void processChar(char ch) throws IOException {
            if (ch != '\t' && ch != '\r' && ch != '\n') {
                wr.write(ch);
                length++;
            }
        }
        @Override
        public void flush() throws IOException {
        }
        @Override
        public void close() throws IOException {
        }
        private int getLength() {
            return length;
        }
        private void complete() throws IOException {
            wr.flush();
            wr.close();
        }
    }
}
