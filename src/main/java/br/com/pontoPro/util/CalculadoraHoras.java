//package br.com.pontoPro.util;
//
//import java.time.LocalTime;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//
//import br.com.pontoPro.model.Atraso;
//import br.com.pontoPro.model.HoraExtra;
//import br.com.pontoPro.model.HorarioTrabalho;
//import br.com.pontoPro.model.Marcacao;
//
//public class CalculadoraHoras
//{
//   public long calcularDiferencaMinutos(String inicio, String fim)
//   {
//      LocalTime inicioTime = LocalTime.parse(inicio);
//      LocalTime fimTime = LocalTime.parse(fim);
//      long diff = ChronoUnit.MINUTES.between(inicioTime, fimTime);
//      return diff;
//   }
//
//   public ArrayList<Atraso> calcularAtrasos(ArrayList<HorarioTrabalho> horariosTrabalho)
//   {
//      ArrayList<Atraso> atrasos = new ArrayList<>();
//
//      for (HorarioTrabalho horario : horariosTrabalho)
//      {
//         LocalTime entradaTrabalho = LocalTime.parse(horario.getEntrada());
//         LocalTime saidaTrabalho = LocalTime.parse(horario.getSaida());
//
//         for (Marcacao marcacao : horario.getMarcacoes())
//         {
//            LocalTime entradaMarcacao = LocalTime.parse(marcacao.getEntrada());
//            LocalTime saidaMarcacao = LocalTime.parse(marcacao.getSaida());
//
//            if (entradaMarcacao.isAfter(entradaTrabalho))
//            {
//               long atraso = calcularDiferencaMinutos(entradaTrabalho.toString(), entradaMarcacao.toString());
//               atrasos.add(new Atraso(horario.getEntrada(), marcacao.getEntrada(), atraso));
//            }
//            if (saidaMarcacao.isBefore(saidaTrabalho))
//            {
//               long atraso = calcularDiferencaMinutos(saidaMarcacao.toString(), saidaTrabalho.toString());
//               atrasos.add(new Atraso(marcacao.getSaida(), horario.getSaida(), atraso));
//            }
//         }
//      }
//
//      return atrasos;
//   }
//
//   public ArrayList<HoraExtra> calcularHorasExtras(ArrayList<HorarioTrabalho> horariosTrabalho)
//   {
//      ArrayList<HoraExtra> horasExtras = new ArrayList<>();
//
//      for (HorarioTrabalho horario : horariosTrabalho)
//      {
//         LocalTime entradaTrabalho = LocalTime.parse(horario.getEntrada());
//         LocalTime saidaTrabalho = LocalTime.parse(horario.getSaida());
//
//         for (Marcacao marcacao : horario.getMarcacoes())
//         {
//            LocalTime entradaMarcacao = LocalTime.parse(marcacao.getEntrada());
//            LocalTime saidaMarcacao = LocalTime.parse(marcacao.getSaida());
//
//            if (entradaMarcacao.isBefore(entradaTrabalho))
//            {
//               long horaExtra = calcularDiferencaMinutos(entradaMarcacao.toString(), entradaTrabalho.toString());
//               horasExtras.add(new HoraExtra(entradaMarcacao.toString(), entradaTrabalho.toString(), horaExtra));
//            }
//            if (saidaMarcacao.isAfter(saidaTrabalho))
//            {
//               long horaExtra = calcularDiferencaMinutos(saidaTrabalho.toString(), saidaMarcacao.toString());
//               horasExtras.add(new HoraExtra(saidaTrabalho.toString(), saidaMarcacao.toString(), horaExtra));
//            }
//         }
//      }
//
//      return horasExtras;
//   }
//}