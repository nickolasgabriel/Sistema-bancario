package cursoHoraDeCodar.projetos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;

public class SistemaBancario {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String usuarioAtual = "Usuario";
        String senha = "1234";

        String chavePixUsuario2 = "testando@gmail.com";

        double saldoDaConta = 1000d;
        ArrayList<Double> extrato = new ArrayList<>();
        ArrayList<String> extratoDestinatario = new ArrayList<>();

        ArrayList<LocalDateTime> dataHoraExtrato = new ArrayList<>();
        DateTimeFormatter formatarData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH.mm");

        efetuarLogin(scanner, usuarioAtual, senha);

        int operacao;
        boolean isTerminar = false;

        while (!isTerminar){
            operacao = mostrarMenu(scanner);
            switch (operacao){
                case 1:
                    consultarSaldo(saldoDaConta);
                    break;
                case 2:
                    saldoDaConta = depositar(saldoDaConta, usuarioAtual, scanner, extratoDestinatario, extrato, dataHoraExtrato, formatarData);
                    break;
                case 3:
                    saldoDaConta = sacar(saldoDaConta, scanner, extrato, extratoDestinatario, dataHoraExtrato, formatarData);
                    break;
                case 4:
                    saldoDaConta = transferir(saldoDaConta, scanner, chavePixUsuario2, extratoDestinatario, extrato, dataHoraExtrato, formatarData);
                    break;
                case 5:
                    verExtrato(extratoDestinatario, extrato, usuarioAtual, chavePixUsuario2, dataHoraExtrato);
                    break;
                case 6:
                    isTerminar = true;
                    break;
            }
        }

    }

    public static void efetuarLogin(Scanner scanner, String usuarioAtual, String senha){
        int tentativasDeLogin = 3;
        boolean isLoginDeuCerto = false;

        while(!isLoginDeuCerto){
            System.out.println("======== LOGIN ========");
            System.out.print("Usuário: ");
            String usuarioTemp = scanner.nextLine();
            System.out.print("Senha: ");
            String senhaTemp = scanner.nextLine();

            if (usuarioTemp.equals(usuarioAtual) && senhaTemp.equals(senha)){
                System.out.println("Login efetuado com sucesso!");
                isLoginDeuCerto = true;
            }else if (tentativasDeLogin == 0){
                System.out.println("Conta bloqueada!");
                System.exit(1);
            }else {
                System.out.println("Usuário ou senha incorreto!");
            }

            tentativasDeLogin--;
        }
    }

    public static int mostrarMenu(Scanner scanner){
        int operacaoEscolhida;
        System.out.println("======== BANCO ========");
        System.out.println("1 - Consultar saldo");
        System.out.println("2 - Depositar");
        System.out.println("3 - Sacar");
        System.out.println("4 - Transferir");
        System.out.println("5 - Ver extrato");
        System.out.println("6 - Sair\n");
        do {
            System.out.print("Escolha uma das opções acima: ");
            while(!scanner.hasNextInt()){
                System.out.print("Erro! Digite apenas números inteiros: ");
                scanner.nextLine();
            }
            operacaoEscolhida = scanner.nextInt();
            scanner.nextLine();
        }while (operacaoEscolhida < 1 || operacaoEscolhida > 6);

        return operacaoEscolhida;
    }

    public static void consultarSaldo(double saldoDaConta){
        System.out.println("Seu saldo é: " + saldoDaConta);
    }

    public static double depositar(double saldoDaConta, String usuarioDaConta, Scanner scanner, ArrayList usuario, ArrayList extrato, ArrayList dataHoraExtrato, DateTimeFormatter formatarData){
        double valorDepositado;

        valorDepositado = verificarValor(scanner);

        dataHoraExtrato.add(LocalDateTime.now().format(formatarData));
        extrato.add(valorDepositado);
        usuario.add(usuarioDaConta);

        return saldoDaConta += valorDepositado;
    }

    public static double sacar(double saldoDaConta, Scanner scanner, ArrayList extrato, ArrayList usuario, ArrayList dataHoraExtrato, DateTimeFormatter formatarData){
        double valorSacado;

        valorSacado = verificarValor(scanner);

        if (valorSacado > saldoDaConta){
            System.out.println("Saldo insuficiente!");
            return saldoDaConta;
        }
        dataHoraExtrato.add(LocalDateTime.now().format(formatarData));
        extrato.add(valorSacado);
        usuario.add(null);

        return saldoDaConta -= valorSacado;
    }

    public static double transferir(double saldoDaConta, Scanner scanner, String chavePix, ArrayList usuario, ArrayList extrato, ArrayList dataHoraExtrato, DateTimeFormatter formatarData){
        double valorTransferido;
        String chavePixDigitada;
        boolean isChaveCorreta = false;

        System.out.print("Chave pix do destinatário: ");
        chavePixDigitada = scanner.next();
        scanner.nextLine();
        while (!isChaveCorreta){
            if (chavePixDigitada.equals(chavePix)){
                isChaveCorreta = true;
            }else {
                System.out.println("Chave pix inválida!");
                System.out.print("Digite uma chave pix válida: ");
                chavePixDigitada = scanner.next();
                scanner.nextLine();
            }
        }

        valorTransferido = verificarValor(scanner);
        if (valorTransferido > saldoDaConta){
            System.out.println("Saldo insuficiente!");
            return saldoDaConta;
        }

        System.out.println("Transferência realizada para " + chavePixDigitada);
        dataHoraExtrato.add(LocalDateTime.now().format(formatarData));
        extrato.add(-valorTransferido);
        usuario.add(chavePixDigitada);

        return saldoDaConta -= valorTransferido;
    }

    public static void verExtrato(ArrayList usuario, ArrayList extrato, String usuarioAtual, String chavePixUsuario2, ArrayList dataHoraExtrato){

        for (int i = 0; i < extrato.size(); i++){
            if (chavePixUsuario2.equals(usuario.get(i))){
                System.out.println(dataHoraExtrato.get(i) + " - Transferência para " + usuario.get(i) + ": " + extrato.get(i));
            } else if (usuarioAtual.equals(usuario.get(i))) {
                System.out.println(dataHoraExtrato.get(i) + " - Deposito: +" + extrato.get(i));
            }else {
                System.out.println(dataHoraExtrato.get(i) + " - Saque: -" + extrato.get(i));
            }
        }
    }

    public static double verificarValor(Scanner scanner){
        double valor;
        do {
            System.out.print("Digite um valor: ");
            while(!scanner.hasNextDouble()){
                System.out.print("Erro! Digite um valor válido: ");
                scanner.nextLine();
            }
            valor = scanner.nextDouble();
            scanner.nextLine();
        }while (valor <= 0);

        return valor;
    }


}
