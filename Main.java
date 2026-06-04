import db.ConexaoDB;
import estrutura.Hospital;
import estrutura.Recepcionista;
import console.MenuConsole;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HOSPITAL SANTA EMERGÊNCIA ===");
        System.out.println("Iniciando sistema...");


        Hospital hospital = new Hospital("Hospital Santa Emergencia");

        Recepcionista recepcionista = new Recepcionista("Ana", "00000000000", 30, "admin", "1234");
        hospital.setRecepcionista(recepcionista);

        MenuConsole menu = new MenuConsole(hospital);
        menu.iniciar();

        System.out.println("Sistema encerrado.");

        ConexaoDB.fecharConexao();
    }
}
