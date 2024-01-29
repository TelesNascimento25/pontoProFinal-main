package br.com.pontoPro.model;

public class HoraExtra
{
   private String entrada;

   private String saida;

   private long horaExtra;
   
   public HoraExtra(String entrada, String saida, long horaExtra) {
      this.entrada = entrada;
      this.saida = saida;
      this.horaExtra = horaExtra;
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

   public long getHoraExtra()
   {
      return horaExtra;
   }

   public void setHoraExtra(long horaExtra)
   {
      this.horaExtra = horaExtra;
   }

}