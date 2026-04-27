package io.github.asmolenkov.tennismatchscoreboard.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/new-math")
public class NewMathController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/NewMatch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameOnePlayer = req.getParameter("name1");
        String nameSecondPlayer = req.getParameter("name2");
        List<String> errorMessage = new ArrayList<>();
        if(nameOnePlayer.trim().isEmpty() || nameOnePlayer.length() > 30 ){
            errorMessage.add("Некорректное имя Игрока #1!");
        }

        if(nameSecondPlayer.trim().isEmpty() || nameSecondPlayer.length() > 30 ){
            errorMessage.add("Некорректное имя Игрока #2!");
        }

        if(!errorMessage.isEmpty()){
            req.setAttribute("error",errorMessage);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.getRequestDispatcher("/WEB-INF/views/NewMatch.jsp").forward(req, resp);
            return;
        }


        req.getRequestDispatcher("/WEB-INF/views/MatchScore.jsp").forward(req,resp);
    }
}
