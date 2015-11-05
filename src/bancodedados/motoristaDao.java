package bancodedados;

import atributos.atributos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EagleTech
 * 
 * Classe de manipulação do banco de dados referente a tabela motoristas
 */
public class motoristaDao {
    
    private Connection conexao;
    
     public motoristaDao() throws SQLException{
        this.conexao = criarConexao.getConexao();
    }
     
       public void cadastroMotorista (atributos user) throws SQLException{
        String sql = "insert into motoristas (nome,dia_nasc,mes_nasc,ano_nasc,dtnasc,rg,cpf" +
                ",cnh,cat,dia_venc,mes_venc,ano_venc,dtvenc,"+
                "opvenc,carreta,anocar,modelo,placa,anoplaca,capton,turno,periodo,telres,telcom,cel,id,"+
                "telrec,email,rua,numero,comp,cep,bairro,"+
                "cidade,uf,pais) values (?,?,?,?,?,?,?,?,?,?,?"+
                ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        stmt.setString(1, user.getNome());
        stmt.setString(2, user.getDiaNasc());
        stmt.setString(3, user.getMesNasc());
        stmt.setString(4, user.getAnoNasc());
        stmt.setString(5, user.getDtNasc());
        stmt.setString(6, user.getRg());
        stmt.setString(7, user.getCpf());
        stmt.setString(8, user.getCnh());
        stmt.setString(9, user.getCat());
        stmt.setString(10, user.getDiaVenc());
        stmt.setString(11, user.getMesVenc());
        stmt.setString(12, user.getAnoVenc());
        stmt.setString(13, user.getDtVenc());
        stmt.setString(14, user.getOpVenc());
        stmt.setString(15, user.getCarreta());
        stmt.setString(16, user.getAnoCarreta());
        stmt.setString(17, user.getModelo());
        stmt.setString(18, user.getPlaca());
        stmt.setString(19, user.getAnoPlaca());
        stmt.setString(20, user.getCapTon());
        stmt.setString(21, user.getTurno());
        stmt.setString(22, user.getPeriodo());
        stmt.setString(23, user.getTelRes());
        stmt.setString(24, user.getTelCom());
        stmt.setString(25, user.getCel());
        stmt.setString(26, user.getId()); 
        stmt.setString(27, user.getTelRec());
        stmt.setString(28, user.getEmail());
        stmt.setString(29, user.getRua());
        stmt.setString(30, user.getNumero());
        stmt.setString(31, user.getCompl());
        stmt.setString(32, user.getCep());
        stmt.setString(33, user.getBairro());
        stmt.setString(34, user.getCidade());
        stmt.setString(35, user.getUf());
        stmt.setString(36, user.getPais());
       stmt.execute();
       stmt.close();
       
    }
       
       public void excluirMotorista (atributos user) throws SQLException {
        String sql = "Delete from motoristas where cod=?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setLong(1, user.getCodigoMotorista());
        stmt.execute();
        stmt.close();
        
    }
       
       public void alteraMotorista(atributos user) throws SQLException{
        String sql = "update motoristas set nome=?,dia_nasc=?,mes_nasc=?,ano_nasc=?,dtnasc=?,rg=?,cpf=?" +
                ",cnh=?,cat=?,dia_venc=?,mes_venc=?,ano_venc=?,dtvenc=?,"+
                "opvenc=?,carreta=?,anocar=?,modelo=?,placa=?,anoplaca=?,capton=?,turno=?,periodo=?,telres=?,telcom=?,cel=?,id=?,"+
                "telrec=?,email=?,rua=?,numero=?,comp=?,cep=?,bairro=?,"+
                "cidade=?,uf=?,pais=? where cod=?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        
        stmt.setString(1, user.getNome());
        stmt.setString(2, user.getDiaNasc());
        stmt.setString(3, user.getMesNasc());
        stmt.setString(4, user.getAnoNasc());
        stmt.setString(5, user.getDtNasc());
        stmt.setString(6, user.getRg());
        stmt.setString(7, user.getCpf());
        stmt.setString(8, user.getCnh());
        stmt.setString(9, user.getCat());
        stmt.setString(10, user.getDiaVenc());
        stmt.setString(11, user.getMesVenc());
        stmt.setString(12, user.getAnoVenc());
        stmt.setString(13, user.getDtVenc());
        stmt.setString(14, user.getOpVenc());
        stmt.setString(15, user.getCarreta());
        stmt.setString(16, user.getAnoCarreta());
        stmt.setString(17, user.getModelo());
        stmt.setString(18, user.getPlaca());
        stmt.setString(19, user.getAnoPlaca());
        stmt.setString(20, user.getCapTon());
        stmt.setString(21, user.getTurno());
        stmt.setString(22, user.getPeriodo());
        stmt.setString(23, user.getTelRes());
        stmt.setString(24, user.getTelCom());
        stmt.setString(25, user.getCel());
        stmt.setString(26, user.getId()); 
        stmt.setString(27, user.getTelRec());
        stmt.setString(28, user.getEmail());
        stmt.setString(29, user.getRua());
        stmt.setString(30, user.getNumero());
        stmt.setString(31, user.getCompl());
        stmt.setString(32, user.getCep());
        stmt.setString(33, user.getBairro());
        stmt.setString(34, user.getCidade());
        stmt.setString(35, user.getUf());
        stmt.setString(36, user.getPais());
        stmt.setLong(37, user.getCodigoMotorista());
        
        stmt.execute();
        stmt.close();
    }
       
        public void alteraOpVenc(atributos user) throws SQLException{
        String sql = "update motoristas set opvenc=? where cod=?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        
        
        stmt.setString(1, user.getOpVenc());
        stmt.setLong(2, user.getCodigoMotorista());
        
        stmt.execute();
        stmt.close();
    }
       
        public List <atributos> getListaNomeMotorista(String nome) throws SQLException{
        String sql = "select *from motoristas where nome like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, nome);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
        
        public List <atributos> getListaTurno(String turno) throws SQLException{
        String sql = "select *from motoristas where turno like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, turno);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
        
        public List <atributos> getListaPlaca(String placa) throws SQLException{
        String sql = "select *from motoristas where placa like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, placa);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
        
         public List <atributos> getListaModelo(String modelo) throws SQLException{
        String sql = "select *from motoristas where modelo like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, modelo);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
         
          public List <atributos> getListaCnh(String cnh) throws SQLException{
        String sql = "select *from motoristas where cnh like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, cnh);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
          
          public List <atributos> getListaOpVenc(String opvenc) throws SQLException{
        String sql = "select *from motoristas where opvenc like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, opvenc);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
          
           public List <atributos> getListaDt(String dtvenc) throws SQLException{
        String sql = "select *from motoristas where dtvenc like ? order by nome";
         PreparedStatement stmt = this.conexao.prepareStatement(sql);
         stmt.setString(1, dtvenc);
         ResultSet rs = stmt.executeQuery();
         
         List<atributos> minhaLista = new ArrayList<atributos>();
         
         while(rs.next()){
             atributos at = new atributos();
             at.setCodigoMotorista(Long.valueOf(rs.getString("cod")));
             at.setNome(rs.getString("nome"));
             at.setDiaNasc(rs.getString("dia_nasc"));
             at.setMesNasc(rs.getString("mes_nasc"));  
             at.setAnoNasc(rs.getString("ano_nasc")); 
             at.setDtNasc(rs.getString("dtnasc"));
             at.setRg(rs.getString("rg"));
             at.setCpf(rs.getString("cpf"));
             at.setCnh(rs.getString("cnh"));
             at.setCat(rs.getString("cat"));
             at.setDiaVenc(rs.getString("dia_venc"));
             at.setMesVenc(rs.getString("mes_venc"));
             at.setAnoVenc(rs.getString("ano_venc"));
             at.setDtVenc(rs.getString("dtvenc"));
             at.setOpVenc(rs.getString("opvenc"));
             at.setCarreta(rs.getString("carreta"));
             at.setAnoCarreta(rs.getString("anocar"));
             at.setModelo(rs.getString("modelo"));
             at.setPlaca(rs.getString("placa"));
             at.setAnoPlaca(rs.getString("anoplaca"));
             at.setCapTon(rs.getString("capton"));
             at.setTurno(rs.getString("turno"));
             at.setPeriodo(rs.getString("periodo"));
             at.setTelRes(rs.getString("telres"));
             at.setTelCom(rs.getString("telcom"));
             at.setCel(rs.getString("cel"));
             at.setId(rs.getString("id"));
             at.setTelRec(rs.getString("telrec"));
             at.setEmail(rs.getString("email"));
             at.setRua(rs.getString("rua"));
             at.setNumero(rs.getString("numero"));
             at.setCompl(rs.getString("comp"));
             at.setCep(rs.getString("cep"));
             at.setBairro(rs.getString("bairro"));
             at.setCidade(rs.getString("cidade"));
             at.setUf(rs.getString("uf"));
             at.setPais(rs.getString("pais"));
             minhaLista.add(at);
         }
         rs.close();
         stmt.close();
         return minhaLista;
    }
    
}
