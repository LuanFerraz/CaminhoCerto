package caminhocerto;

import java.util.Date;

public class Data {

    public int mes, diaSemana;
    public String xMes, xDia, ano, dia;

    public void data() {
        Date data = new Date();
        mes = data.getMonth();
        dia = "" + data.getDate();
        ano = "" + (1900 + data.getYear());
        diaSemana = data.getDay();
        switch (diaSemana) {   //converte o valor numerico do dia da semana em uma String
            case 0:
                xDia = "Domingo";
                break;
            case 1:
                xDia = "Segunda";
                break;
            case 2:
                xDia = "Terça";
                break;
            case 3:
                xDia = "Quarta";
                break;
            case 4:
                xDia = "Quinta";
                break;
            case 5:
                xDia = "Sexta";
                break;
            case 6:
                xDia = "Sabado";
                break;
        }
        switch (mes) {   //Converte o valor numerico do mes em String
            case 0:
                xMes = "Janeiro";
                break;
            case 1:
                xMes = "Fevereiro";
                break;
            case 2:
                xMes = "Março";
                break;
            case 3:
                xMes = "Abril";
                break;
            case 4:
                xMes = "Maio";
                break;
            case 5:
                xMes = "Junho";
                break;
            case 6:
                xMes = "Julho";
                break;
            case 7:
                xMes = "Agosto";
                break;
            case 8:
                xMes = "Setembro";
                break;
            case 9:
                xMes = "Outubro";
                break;
            case 10:
                xMes = "Novembro";
                break;
            case 11:
                xMes = "Dezembro";
                break;
        }
    }
}