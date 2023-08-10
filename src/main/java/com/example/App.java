package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.example.db.DB;
import com.example.db.DbException;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("CRUD DE CONTATOS");
        Connection conn = null;
        Statement st = null;

        try {
            conn = DB.getConnection();
            st = conn.createStatement();
            System.out.println("================= SELECIONE O QUE DESEJA: =================");
            System.out.println("1 - Listar todos");
            System.out.println("2 - Cadastrar");
            System.out.println("3 - Deletar");
            System.out.println();
            int opc = sc.nextInt();
            sc.nextLine();
            System.out.println();
            switch (opc) {
                case 1:
                    findAll(st);
                    break;

                case 2:
                    cadastrar(sc, sdf, sdf2, st);
                    break;

                case 3:
                    deleteById(sc, st);
                    break;

                default:
                    System.out.println("Opção inválida. Rode o programa novamente!!!!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } catch (ParseException e) {
            throw new DbException(e.getMessage());
        } finally {
            sc.close();
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
            DB.closeConnection();
        }

    }

    private static void deleteById(Scanner sc, Statement st) throws SQLException {
        System.out.println("================= DELETAR CONTATO POR ID DO BD =================");
        System.out.print("Digite o ID do contato: ");
        int id = sc.nextInt();
        sc.nextLine();

        st.executeUpdate("DELETE FROM contatos WHERE contatos.id =" + id + ";");

        System.out.println("Adicionado com sucesso o vendedor de ID " + id);    }

    private static void findAll(Statement st) throws SQLException {
        ResultSet rs;
        rs = st.executeQuery("SELECT * FROM contatos;");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + " - NOME: " + rs.getString("nome"));
        }
    }

    private static void cadastrar(Scanner sc, SimpleDateFormat sdf, SimpleDateFormat sdf2, Statement st)
            throws ParseException, SQLException {
        System.out.println("================= CADASTRAR NOVO CONTATO NO BD =================");
        System.out.print("Digite o nome do contato: ");
        String nome = sc.nextLine();
        System.out.print("Digite o sobrenome do contato: ");
        String sobrenome = sc.nextLine();
        System.out.print("Digite o email do contato: ");
        String email = sc.nextLine();
        System.out.print("Digite o telefone do contato: ");
        String telefone = sc.nextLine();
        System.out.print("Digite a data de nascimento do contato (dd/MM/yy): ");
        Date birthDate = sdf.parse(sc.nextLine());
        String dataFormatada = sdf2.format(birthDate);

        st.executeUpdate(
                "INSERT INTO contatos "
                        + "(nome, sobrenome, email, telefone, data_nascimento) "
                        + "VALUES ("
                        + "'" + nome + "', "
                        + "'" + sobrenome + "', "
                        + "'" + email + "', "
                        + "'" + telefone + "', "
                        + "'" + dataFormatada + "');"

        );

        System.out.println("Adicionado com sucesso o vendedor " + nome);
    }
}
