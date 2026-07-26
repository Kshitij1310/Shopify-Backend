package controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadsController {

    private static final String UPLOADS_DIR = "uploads";

    /** DEV ONLY: echo the caller's origin back so any dev-server port works. */
    private static String allowedOrigin(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        return origin != null ? origin : "*";
    }

    @RequestMapping(path = "/uploads/**", method = {RequestMethod.GET, RequestMethod.OPTIONS})
    public ResponseEntity<Resource> serveUpload(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // strip leading /uploads/
        String relative = uri.replaceFirst("/uploads/", "");
        Path file = Paths.get(UPLOADS_DIR).resolve(relative).normalize();

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Origin", allowedOrigin(request));
            headers.add("Access-Control-Allow-Methods", "GET, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
            headers.add("Access-Control-Allow-Credentials", "true");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }

        if (!Files.exists(file) || Files.isDirectory(file)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(file.toUri());
            String contentType = Files.probeContentType(file);
            MediaType mediaType = contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;

            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Origin", allowedOrigin(request));
            headers.add("Access-Control-Allow-Credentials", "true");

            return ResponseEntity.ok().headers(headers).contentType(mediaType).body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
