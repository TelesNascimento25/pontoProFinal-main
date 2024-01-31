package br.com.pontoPro.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.pontoPro.model.HorarioTrabalho;

@WebServlet("/AdicionarRegistroServlet")
public class AdicionarRegistroServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter dados do formulário
        String entrada = request.getParameter("entrada");
        String saida = request.getParameter("saida");

        // Validar dados, realizar lógica de adição e obter os dados atualizados
        // ...

        // Enviar resposta JSON para o cliente
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("registro", new HorarioTrabalho(entrada, saida));
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(responseData));
    }
}