package br.com.pontoPro.model;

public class Atraso
{
   private String entrada;

   private String saida;

   private long atraso;

   public Atraso(String entrada, String saida, long atraso) {
      this.entrada = entrada;
      this.saida = saida;
      this.atraso = atraso;
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

   public long getAtraso()
   {
      return atraso;
   }

   public void setAtraso(long atraso)
   {
      this.atraso = atraso;
   }

}