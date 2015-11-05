package bancodedados;

import atributos.atributos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class usuarioDao {
    
    private Connection conexao;
    
    public usuarioDao() throws SQLException{
        this.conexao = criarConexao.getConexao();
    }
    
    public void cadastroUsuario (atributos user) throws SQLException{
        String sql = "insert into usuario (nome_usuario,login_usuario,senha_usuario," +
                "confirma_senha,tipo_usuario) values (?,?,?,?,?)";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, user.getNomeUsuario());
        stmt.setString(2, user.getLogin());
        stmt.setString(3, user.getSenha());
        stmt.setString(4, user.getConfirmaSenha());
        stmt.setString(5, user.getNivelUsuario());
        
       stmt.execute();
       stmt.close();
       
    }
    
      public void excluirUsuario (atributos at) throws SQLException {
        String sql = "Delete from usuario where codigo_usuario=?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setLong(1, at.getCodigoUsuario());
        stmt.execute();
        stmt.close();
        
    }
      
      
      
      public void alteraUsuario(atributos at) throws SQLException{
        String sql = "update usuario set nome_usuario=?," +
                "login_usuario=?,senha_usuario=?,confirma_senha=?" +
                ",tipo_usuario=? where codigo_usuario=?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        
        stmt.setString(1, at.getNomeUsuario());
        stmt.setString(2, at.getLogin());
        stmt.setString(3, at.getSenha());
        stmt.setString(4, at.getConfirmaSenha());
        stmt.setString(5, at.getNivelUsuario());
        stmt.setLong(6, at.getCodigoUsuario());
        
        
        stmt.execute();
        stmt.close();
    }
      
      
   public List <atributos> getListaNome(String nome) throws SQLException{
        String sql = "select *from usuario where nome_usuario like ? order by nome_usuario";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, nome);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoUsuario(Long.valueOf(rs.getString("codigo_usuario")));
             at.setNomeUsuario(rs.getString("nome_usuario"));
             at.setLogin(rs.getString("login_usuario"));
             at.setSenha(rs.getString("senha_usuario"));
             at.setConfirmaSenha(rs.getString("confirma_senha"));
             at.setNivelUsuario(rs.getString("tipo_usuario"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
    
public List <atributos> getListaCodigo(String codigo) throws SQLException{
        String sql = "select *from usuario where codigo_usuario like ?";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, codigo);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoUsuario(Long.valueOf(rs.getString("codigo_usuario")));
             at.setNomeUsuario(rs.getString("nome_usuario"));
             at.setLogin(rs.getString("login_usuario"));
             at.setSenha(rs.getString("senha_usuario"));
             at.setConfirmaSenha(rs.getString("confirma_senha"));
             at.setNivelUsuario(rs.getString("tipo_usuario"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
    
public List <atributos> getListaLogin(String login) throws SQLException{
        String sql = "select *from usuario where login_usuario like ? order by nome_usuario";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, login);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoUsuario(Long.valueOf(rs.getString("codigo_usuario")));
             at.setNomeUsuario(rs.getString("nome_usuario"));
             at.setLogin(rs.getString("login_usuario"));
             at.setSenha(rs.getString("senha_usuario"));
             at.setConfirmaSenha(rs.getString("confirma_senha"));
             at.setNivelUsuario(rs.getString("tipo_usuario"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }

public List <atributos> getListaTipo(String tipo) throws SQLException{
        String sql = "select *from usuario where tipo_usuario like ? order by nome_usuario";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, tipo);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoUsuario(Long.valueOf(rs.getString("codigo_usuario")));
             at.setNomeUsuario(rs.getString("nome_usuario"));
             at.setLogin(rs.getString("login_usuario"));
             at.setSenha(rs.getString("senha_usuario"));
             at.setConfirmaSenha(rs.getString("confirma_senha"));
             at.setNivelUsuario(rs.getString("tipo_usuario"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
}
