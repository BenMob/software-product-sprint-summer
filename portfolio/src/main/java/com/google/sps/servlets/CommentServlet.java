// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.services.CommentService;
import com.google.sps.entities.Comment;
import com.google.sps.entities.User;
import com.google.sps.services.AuthenticationService;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comments")
public class CommentServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Comment> comments = CommentService.getAllComments();
    response.setContentType("application/json;");
    response.getWriter().println(CommentService.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    UserService userService = UserServiceFactory.getUserService();
    if(userService.isUserLoggedIn()){
        User currentUser = AuthenticationService.queryUserById(userService
        .getCurrentUser()
        .getUserId());

        if(currentUser.getId() == 0){
            response.sendRedirect("register.html");
        }else{
            final Entity commentEntity = CommentService.createCommentEntity(request, currentUser);
            CommentService.save(commentEntity);
            response.sendRedirect("index.html#comments");
        }
    }else response.sendRedirect(userService.createLoginURL("/register.html"));
  }
}






