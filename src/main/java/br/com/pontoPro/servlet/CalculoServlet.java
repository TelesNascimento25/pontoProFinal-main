package br.com.pontoPro.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.pontoPro.model.Atraso;
import br.com.pontoPro.model.HoraExtra;
import br.com.pontoPro.model.HorarioTrabalho;
import br.com.pontoPro.model.Marcacao;
import br.com.pontoPro.util.CalculadoraHoras;

@WebServlet("/calculo")
public class CalculoServlet extends HttpServlet
{
   private static final long serialVersionUID = 977169726755805767L;

   public ArrayList<Atraso> content;
   
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
       String entrada = request.getParameter("entrada");
       String saida = request.getParameter("saida");
       String entradaMarcacao = request.getParameter("entradaMarcacao");
       String saidaMarcacao = request.getParameter("saidaMarcacao");

       Marcacao marcacao = new Marcacao(entradaMarcacao, saidaMarcacao);
       ArrayList<Marcacao> marcacoes = new ArrayList<>();
       marcacoes.add(marcacao);

       HorarioTrabalho horarioTrabalho = new HorarioTrabalho(entrada, saida, marcacoes);
       ArrayList<HorarioTrabalho> horariosTrabalho = new ArrayList<>();
       horariosTrabalho.add(horarioTrabalho);

       CalculadoraHoras calculadora = new CalculadoraHoras();
       ArrayList<Atraso> atrasos = calculadora.calcularAtrasos(horariosTrabalho);
       ArrayList<HoraExtra> horasExtras = calculadora.calcularHorasExtras(horariosTrabalho);

       this.setContent(atrasos);
       request.setAttribute("atrasos", atrasos);
       request.setAttribute("horasExtras", horasExtras);

       response.sendRedirect("plataforma");
   }

   public ArrayList<Atraso> getContent()
   {
      return content;
   }

   public void setContent(ArrayList<Atraso> atrasos)
   {
      this.content = atrasos;
   }


}
