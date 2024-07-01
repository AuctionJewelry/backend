package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.constants.ImageContants;
import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.BlogEntity;
import com.se.jewelryauction.models.BlogImageEntity;
import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.repositories.IBlogImageRepository;
import com.se.jewelryauction.repositories.IBlogRepository;
import com.se.jewelryauction.requests.BlogRequest;
import com.se.jewelryauction.services.IBlogService;
import com.se.jewelryauction.utils.UploadImagesUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BlogService implements IBlogService {
    private final IBlogRepository blogRepository;

    private final IBlogImageRepository blogImageRepository;

    @Override
    public BlogEntity createBlog(BlogRequest blogRequest, List<MultipartFile> images) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        BlogEntity blog = new BlogEntity();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setUser(user);

        BlogEntity savedBlog = blogRepository.save(blog);

        if (images != null && !images.isEmpty()) {
            List<BlogImageEntity> blogImageList = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    BlogImageEntity blogImage = new BlogImageEntity();
                    blogImage.setBlog(savedBlog);
                    blogImage.setUrl(UploadImagesUtils.storeFile(image, ImageContants.BLOG_IMAGE_PATH));
                    blogImageList.add(blogImageRepository.save(blogImage));
                }
            }
            savedBlog.setBlogImages(blogImageList);
        }

        return savedBlog;
    }

    @Override
    public List<BlogEntity> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public void deleteBlog(Long blogId) {
        blogRepository.deleteById(blogId);
    }

    @Override
    public BlogEntity getBlogById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This blog is not existed!"));
    }
}
