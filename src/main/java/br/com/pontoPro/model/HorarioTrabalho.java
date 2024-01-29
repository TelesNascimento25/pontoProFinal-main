package br.com.pontoPro.model;

import java.util.ArrayList;

public class HorarioTrabalho
{
   private String entrada;

   private String saida;

   private ArrayList<Marcacao> marcacoes;

   public HorarioTrabalho(String entrada, String saida, ArrayList<Marcacao> marcacoes) {
      this.entrada = entrada;
      this.saida = saida;
      this.marcacoes = marcacoes;
  }
   
   public String getEntrada()
   {
      return entrada;
   }

   public void setEntrada(String entrada)
   {
      this.entrada = entrada;
   }

   public String getSaida()
   {
      return saida;
   }

   public void setSaida(String saida)
   {
      this.saida = saida;
   }

   public ArrayList<Marcacao> getMarcacoes()
   {
      return marcacoes;
   }

   public void setMarcacoes(ArrayList<Marcacao> marcacoes)
   {
      this.marcacoes = marcacoes;
   }

}