package org.sp.project0713.member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/*
JDBC - 자바 언어로 데이터베이스를 제어하는 기술을 가리킴
Java Database Connectivity
java.sql 패키지에서 지원함


*/

class  JoinForm extends JFrame implements ActionListener{
	JTextField t_id;
	JTextField t_name;
	JTextField t_phone;
	JButton bt_connect; //DB접속용
	JButton bt_regist; //등록

	String url="jdbc:mysql://localhost:3306/javase?characterEncoding=utf8"; //포트번호임 오라클 1521
	String url2="jdbc:oracle:thin:@localhost:1521:XE"; //오라클 접속

	//Connection 객체란? -> 접속 성공 시 그 접속 정보를 보유한 객체
	Connection con=null;

	public JoinForm(){
		t_id=new JTextField();
		t_name=new JTextField();
		t_phone=new JTextField();
		bt_connect=new JButton("접속");
		bt_regist=new JButton("가입");

		Dimension d=new Dimension(280, 40);

		t_id.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_phone.setPreferredSize(d);

		setLayout(new FlowLayout());
		
		add(t_id);
		add(t_name);
		add(t_phone);
		add(bt_connect);
		add(bt_regist);

		setSize(300, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//버튼들과 리스너 연결
		bt_connect.addActionListener(this);
		bt_regist.addActionListener(this);

		bt_regist.setEnabled(false);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//1) 열려있는 커넥션 닫기
				if(con!=null){
					try{
						con.close();
					}catch(SQLException e2){
						e2.printStackTrace();
					}
					
				}

				//2) 프로세스 종료
				System.exit(0);
			}
	

		});

	}

	//mysql 데이터베이스에 접속 시도
	public void connect(){

		//Mysql용 드라이버를 로드함
		try{
			Class.forName("com.mysql.jdbc.Driver"); //!!!!!수정해야함!!!!!
			System.out.println("로드 성공");

			//접속을 시도함
			//Connection 객체란? -> 접속 성공 시 그 접속 정보를 보유한 객체
			//Connection con=null;

			//쿼리문 수행을 담당하는 jdbc객체
			//PreparedStatement pstmt=null;

			//con=DriverManager.getConnection(url, "root", "1234"); //주소 아이디 비번 mysql꺼
			con=DriverManager.getConnection(url2, "java", "1234"); //오라클꺼

			if(con==null){
				System.out.println("실패ㅠㅠ");
			}else{
				System.out.println("성공^^");

				//접속 버튼 비활성화 & 가입 버튼 활성화
				bt_connect.setEnabled(false);
				bt_regist.setEnabled(true);
			}

		}catch(ClassNotFoundException e){
			System.out.println("드라이버가 존재하지 않음");
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	//등록하기
	public void regist(){
		//쿼리문을 수행해보자

		String id=t_id.getText();
		String name=t_name.getText();
		String phone=t_phone.getText();

		String sql="insert into member(id, name, phone) values('"+id+"', '"+name+"', '"+phone+"')";
		PreparedStatement pstmt=null; //쿼리문 수행을 담당하는 jdbc객체

		try{
			pstmt=con.prepareStatement(sql);
			//준비된 쿼리문을 실행하기
			//executeUpdate() 메서드는 DML을 수행할 수 있는데, 이때 수행결과로 이 쿼리 실행에 의해 영향을 받은 레코드의 수를 반환함
			//따라서 개발자는 그 결과가 0이면 DML수행이 안되었다는 것을 알 수 있음
			
			int result=pstmt.executeUpdate();

			if(result>0){
				JOptionPane.showMessageDialog(this, "가입 성공");
			}

		}catch(SQLException e){
			e.printStackTrace();
		}finally{

			if(pstmt!=null){
				try{
					pstmt.close();	
				}catch(SQLException e){
					e.printStackTrace();
				}
				
			}
			
		}
		
	}

	//오라클에  insert
	public void registOracle(){
		PreparedStatement pstmt=null;
		String sql="insert into memberjava (member_idx, id, name, phone)";
		sql=sql+" values (seq_memberjava.nextval, 'batman', 'bat', '011')";

		try{
			pstmt=con.prepareStatement(sql); //쿼리문 준비
			int result=pstmt.executeUpdate(); //insert실행

			if(result > 0){
				JOptionPane.showMessageDialog(this, "오라클 등록 성공");
			}

		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
	}

	public void actionPerformed(ActionEvent e){
		Object obj=e.getSource();

		if(obj==bt_connect){ //접속 버튼을 누르면
			connect();
		}else if(obj==bt_regist){ //가입 버튼을 누르면
			//regist();
			registOracle();
		}
	}

	public static void main(String[] args){
		new JoinForm();

		//System.out.println("Hello World!");
	}
}
