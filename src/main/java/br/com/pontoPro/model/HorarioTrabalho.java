package br.com.pontoPro.model;


public class HorarioTrabalho
{
   private String entrada;

   private String saida;

   public HorarioTrabalho(String entrada, String saida)
   {
      this.entrada = entrada;
      this.saida = saida;
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

}