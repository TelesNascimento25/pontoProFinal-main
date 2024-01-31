package br.com.pontoPro.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/CalcularAtrasoHoraExtraServlet")
public class CalcularAtrasoHoraExtraServlet extends HttpServlet
{
   private static final long serialVersionUID = -3477739799250801216L;

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
   {
      String horariosTrabalhoJson = request.getParameter("horariosTrabalho");
      String marcacoesTrabalhoJson = request.getParameter("marcacoesTrabalho");

      Type listType = new TypeToken<List<Map<String, String>>>()
      {
      }.getType();
      List<Map<String, String>> horariosTrabalho = new Gson().fromJson(horariosTrabalhoJson, listType);
      List<Map<String, String>> marcacoesTrabalho = new Gson().fromJson(marcacoesTrabalhoJson, listType);

      Collections.sort(horariosTrabalho, new Comparator<Map<String, String>>()
      {
         public int compare(Map<String, String> one, Map<String, String> two)
         {
            return one.get("entrada").compareTo(two.get("entrada"));
         }
      });

      Collections.sort(marcacoesTrabalho, new Comparator<Map<String, String>>()
      {
         public int compare(Map<String, String> one, Map<String, String> two)
         {
            return one.get("entrada").compareTo(two.get("entrada"));
         }
      });

      double[] horariosTrabalhoNumeros = converterParaNumeros(horariosTrabalho);
      double[] marcacoesTrabalhoNumeros = converterParaNumeros(marcacoesTrabalho);

      List<Map<String, Double>> atrasos = new ArrayList<>();
      List<Map<String, Double>> horasExtras = new ArrayList<>();

      if (horariosTrabalhoNumeros.length == 2 && marcacoesTrabalhoNumeros.length == 2)
      {
         double inicioHT = horariosTrabalhoNumeros[0];
         double fimHT = horariosTrabalhoNumeros[1];
         double inicioMT = marcacoesTrabalhoNumeros[0];
         double fimMT = marcacoesTrabalhoNumeros[1];

         if (inicioMT < inicioHT)
         {
            Map<String, Double> resultado = new HashMap<>();
            resultado.put("inicioMarcação", inicioMT);
            resultado.put("inicioHorário", inicioHT);
            resultado.put("totalHoraExtra", inicioHT - inicioMT);
            horasExtras.add(resultado);
         }
         else
         {
            Map<String, Double> resultado = new HashMap<>();
            resultado.put("inicioMarcação", inicioMT);
            resultado.put("inicioHorário", inicioHT);
            resultado.put("totalAtraso", inicioMT - inicioHT);
            atrasos.add(resultado);
         }

         if (fimMT > fimHT)
         {
            Map<String, Double> resultado = new HashMap<>();
            resultado.put("fimMarcação", fimMT);
            resultado.put("fimHorário", fimHT);
            resultado.put("totalHoraExtra", fimMT - fimHT);
            horasExtras.add(resultado);
         }
         else
         {
            Map<String, Double> resultado = new HashMap<>();
            resultado.put("fimMarcação", fimMT);
            resultado.put("fimHorário", fimHT);
            resultado.put("totalAtraso", fimHT - fimMT);
            atrasos.add(resultado);
         }
      }
      else
      {
         // Caso geral com múltiplos Horários de Trabalho e Marcação de Trabalho
         for (int i = 0; i < horariosTrabalhoNumeros.length; i += 2) {
            for (int j = 0; j < marcacoesTrabalhoNumeros.length; j += 2) {
               if (i + 1 < horariosTrabalhoNumeros.length && j + 1 < marcacoesTrabalhoNumeros.length) {
                  double inicioHT = horariosTrabalhoNumeros[i];
                  double fimHT = horariosTrabalhoNumeros[i + 1];
                  double inicioMT = marcacoesTrabalhoNumeros[j];
                  double fimMT = marcacoesTrabalhoNumeros[j + 1];

                  if (inicioMT < inicioHT && !jaCalculado(horasExtras, inicioMT, inicioHT)) {
                     Map<String, Double> resultado = new HashMap<>();
                     resultado.put("inicioMarcação", inicioMT);
                     resultado.put("inicioHorário", inicioHT);
                     resultado.put("totalHoraExtra", inicioHT - inicioMT);
                     horasExtras.add(resultado);
                  } else if (!jaCalculado(atrasos, inicioMT, inicioHT)) {
                     Map<String, Double> resultado = new HashMap<>();
                     resultado.put("inicioMarcação", inicioMT);
                     resultado.put("inicioHorário", inicioHT);
                     resultado.put("totalAtraso", inicioMT - inicioHT);
                     atrasos.add(resultado);
                  }

                  if (fimMT > fimHT && !jaCalculado(horasExtras, fimHT, fimMT)) {
                     Map<String, Double> resultado = new HashMap<>();
                     resultado.put("fimMarcação", fimMT);
                     resultado.put("fimHorário", fimHT);
                     resultado.put("totalHoraExtra", fimMT - fimHT);
                     horasExtras.add(resultado);
                  } else if (!jaCalculado(atrasos, fimHT, fimMT)) {
                     Map<String, Double> resultado = new HashMap<>();
                     resultado.put("fimMarcação", fimMT);
                     resultado.put("fimHorário", fimHT);
                     resultado.put("totalAtraso", fimHT - fimMT);
                     atrasos.add(resultado);
                  }

                  // Caso uma Marcação abrange mais de um Horário de Trabalho
                  if (inicioMT < inicioHT && fimMT > fimHT) {
                     for (int k = i + 2; k < horariosTrabalhoNumeros.length; k += 2) {
                        double novoInicioHT = horariosTrabalhoNumeros[k];
                        double novoFimHT = horariosTrabalhoNumeros[k + 1];

                        if (fimMT > novoFimHT && !jaCalculado(horasExtras, novoInicioHT, novoFimHT)) {
                           Map<String, Double> resultado = new HashMap<>();
                           resultado.put("inicioMarcação", novoInicioHT);
                           resultado.put("fimHorário", novoFimHT);
                           resultado.put("totalHoraExtra", novoFimHT - novoInicioHT);
                           horasExtras.add(resultado);
                        } else if (!jaCalculado(atrasos, novoInicioHT, fimMT)) {
                           Map<String, Double> resultado = new HashMap<>();
                           resultado.put("inicioMarcação", novoInicioHT);
                           resultado.put("fimMarcação", fimMT);
                           resultado.put("totalAtraso", fimMT - novoInicioHT);
                           atrasos.add(resultado);
                           break;
                        }
                     }
                  }
               }
            }
         }

         // Verificar se há horas extras entre o fim de um horário de trabalho e o início do próximo
         for (int i = 0; i < horariosTrabalhoNumeros.length - 2; i += 2) {
            double fimHT = horariosTrabalhoNumeros[i + 1];
            double inicioProximoHT = horariosTrabalhoNumeros[i + 2];

            for (int j = 0; j < marcacoesTrabalhoNumeros.length - 1; j += 2) {
               double fimMT = marcacoesTrabalhoNumeros[j + 1];

               if (fimMT > fimHT && fimMT > inicioProximoHT && !jaCalculado(horasExtras, fimHT, inicioProximoHT)) {
                  Map<String, Double> resultado = new HashMap<>();
                  resultado.put("fimHorário", fimHT);
                  resultado.put("inicioProximoHorário", inicioProximoHT);
                  resultado.put("totalHoraExtra", fimHT - inicioProximoHT);
                  horasExtras.add(resultado);
               }
            }
         }

         Map<String, List<Map<String, Double>>> resultados = new HashMap<>();
         resultados.put("atrasos", atrasos);
         resultados.put("horasExtras", horasExtras);

         response.setContentType("application/json");
         response.getWriter().write(new Gson().toJson(resultados));
      }
   }

      private boolean jaCalculado(List<Map<String, Double>> lista, double inicio, double fim) {
         for (Map<String, Double> item : lista) {
            if (item.get("inicioMarcação") != null && item.get("inicioMarcação") == inicio && item.get("inicioHorário") != null && item.get("inicioHorário") == fim) {
               return true;
            }
            if (item.get("fimMarcação") != null && item.get("fimMarcação") == fim && item.get("fimHorário") != null && item.get("fimHorário") == inicio) {
               return true;
            }
         }
         return false;
      }
   

   private double[] converterParaNumeros(List<Map<String, String>> horarios) {
      double[] horariosNumeros = new double[horarios.size() * 2]; // Dobrar o tamanho para acomodar tanto a entrada quanto a saída
      for (int i = 0; i < horarios.size(); i++) {
         Map<String, String> horario = horarios.get(i);
         double horasEntrada = Double.parseDouble(horario.get("entrada").split(":")[0]);
         double minutosEntrada = Double.parseDouble(horario.get("entrada").split(":")[1]);
         horariosNumeros[i * 2] = horasEntrada + minutosEntrada / 60.0;

         double horasSaida = Double.parseDouble(horario.get("saida").split(":")[0]);
         double minutosSaida = Double.parseDouble(horario.get("saida").split(":")[1]);
         horariosNumeros[i * 2 + 1] = horasSaida + minutosSaida / 60.0;
      }
      return horariosNumeros;
   }
}