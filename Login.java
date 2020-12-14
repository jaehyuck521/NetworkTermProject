package Mypackage;
import java.awt.event.ActionEvent;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.net.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import Mypackage.SocialDate;
public class Login
{
   //
   static String ip=null; //txt파일에서, ip읽어옴.
   static int portnum=0; //port number
    String serverAddress; //서버 주소
    Scanner in; //소켓을 받아들일 매개체
    PrintWriter out; //소켓으로 보낼 매게체
    String curid;    //현재 로그인 id
    
    //login gui variables
    JFrame frame=new JFrame("login form"); //처음 로그인창
    JLabel lbl,la1,la2,la3,emp;
    JTextField id;
    JPasswordField passwd;
    JPanel emptyPanel,idPanel,paPanel,loginPanel;
    JButton b1,b2,b3;
    JTextArea content;
    String host; //채팅방 개설자를 구분하여, 표출하기 위한 구분 변수
    //TODO:
    
    //메뉴창 
    JFrame menu_frame;
   JLabel user_name;
   JLabel user_state;
   JPanel menu_p;
   JButton make_room;
   JButton add_friend;
   JList friend_list;
   DefaultListModel friend_model;
   JButton change_info;
   JPopupMenu popupMenu;
   JMenuItem lookinfo;
   JMenuItem sendMsg;
    
    
    //채팅방 swing 변수
    JFrame room_frame;
   JTextField sendTF; //보낼 메시지 적는 곳
   JLabel la_msg;   
   JTextArea ta;   //메시지가 쌓여서 보이는 곳
   JScrollPane sp_ta,sp_list;  
    JList<String> user;   // 현재 채팅방에 있는 유저 표시
   JButton invite_btn,exit_btn;  //초대 나가기 버튼 
   JPanel p;  
   DefaultListModel user_model;

   
   //추가하려는 친구
    String relaid;
    //비밀번호 암호화 코드
   public String encryptSHA256(String str) {
        String sha="";
        try { MessageDigest sh= MessageDigest.getInstance("SHA-256");
        sh.update(str.getBytes());
        byte[] byteData = sh.digest();
        StringBuilder sb=new StringBuilder();
        for (byte byteDatum : byteData) {
           sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
           } 
           sha=sb.toString(); }
        catch (NoSuchAlgorithmException e) {
           System.out.println("암호화 에러-NoSuchAlgorithmException");
           sha=null;
        }
        return sha;
   }
   //constructor
    public Login(String serverAddress)
    {
         this.serverAddress=serverAddress;
          // FlowLayout사용
          frame.setLayout( new FlowLayout() );
          
          // Border로 영역 생성
          EtchedBorder eborder =  new EtchedBorder();
          // 레이블 생성     
          lbl = new JLabel( "Enter Id and Password" );
          // 레이블에 영역 만들기
          lbl.setBorder(eborder);
          // 레이블 추가
          frame.add(lbl);

          emptyPanel = new JPanel();
          emp = new JLabel("\n");
          emptyPanel.add(emp);
          frame.add(emp);
          
          // id패널과 pw 패널생성
          idPanel = new JPanel();
          paPanel = new JPanel();

          la3 = new JLabel("User ID       ");
          la2 = new JLabel("Password  ");
          // id텍스트필드와 pw텍스트 필드 선언
          id = new JTextField(20);
          passwd = new JPasswordField(20);
          idPanel.add(la3);
          idPanel.add(id);
          idPanel.setBackground(Color.yellow);
          paPanel.add(la2);
          paPanel.add(passwd);
          paPanel.setBackground(Color.yellow);
          
          // 로그인과 회원가입을 위한 패널 생성
          
          loginPanel = new JPanel();
          loginPanel.setBackground(Color.white);
          b1 = new JButton("Login");
          b2 = new JButton("Resister");
          b3 = new JButton("Withdrawal");
          b1.setBackground(Color.yellow);
          b2.setBackground(Color.yellow);
          b3.setBackground(Color.yellow);
          
          loginPanel.add(emp);
          loginPanel.add(b1);
          loginPanel.add(b2);
          loginPanel.add(b3);
          frame.add(idPanel, BorderLayout.WEST);
          frame.add(paPanel, BorderLayout.WEST);
          frame.add(loginPanel);
                    
          b1.addActionListener(new ActionListener() 
          {
             public void actionPerformed(ActionEvent e)
             {
                String comment=e.getActionCommand();
                if(comment.contentEquals("Login")) //로그인 버튼을 눌렀을 때,
                {
                   
                   String logid= id.getText().trim(); //id에 누른 값을 불러옴
                   String logpw= passwd.getText(); //패스워드에 누른 값을 불러옴.
                   String encryptLogPW = encryptSHA256(logpw); //암호화된 비밀번호를 해독해서 받아옴
                   out.println("logid"+logid+" "+encryptLogPW); //서버에게 아이디와 패스워드 전달
                   if(logid.length()==0 || logpw.length()==0) //id와 비밀번호를 아무 것도 입력하지 않았을 때, 에러 메시지를 표출함.
                     {
                     JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 입력 하셔야 됩니다.", "아이디나 비번을 입력!", JOptionPane.DEFAULT_OPTION);
                     return;
                     }
                   frame.dispose();
                }
             }
             });
          b2.addActionListener(new ActionListener() { //회원가입을 눌렀을 때, 회원가입 창으로 이동시켜준다.
         @Override
         public void actionPerformed(ActionEvent e) {
            new Resister();            
         }
          });
          
          b3.addActionListener(new ActionListener() { //회원가입을 눌렀을 때, 회원가입 창으로 이동시켜준다.
         @Override
         public void actionPerformed(ActionEvent e) {
            new Withdrawal();            
         }
          });
          // 3행 20열 영역의 텍스트에어리어 
          //content = new JTextArea(3,20);
          //JScrollPane s= new JScrollPane(content);
          //add(s);
          frame.setSize( 400, 200 );
          frame.getContentPane().setBackground(Color.white);
          frame.setVisible(false);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    //TODO:
    //채팅방 프레임
    public class Chatting_Room {
          public Chatting_Room(String id,String hst,  String friend) 
          {
        	  
            room_frame = new JFrame(id+"'s chatting room"+" host-"+hst); //room frame 상단에 해당 사람의 채팅방이다는 것을 표시 시켜주고, 채팅방의 호스트가 누구인지 알려준다 
            sendTF = new JTextField(15);     
            la_msg = new JLabel("Message");
            ta = new JTextArea();
            ta.setLineWrap(true);//TextArea 가로길이를 벗어나는 text발생시 자동 줄바꿈
            user = new JList(new DefaultListModel()); //채팅방의 리스트를 표시해주고 업데이트 해준다
            user_model = (DefaultListModel)user.getModel();
            //friend = friend.substring(1,friend.length()-1)
            for(String f : friend.split(",")) {  //채팅방의 사용자 출력용
               user_model.addElement(f);
            }
           
            sp_ta = new JScrollPane(ta);
            sp_list = new JScrollPane(user);
            invite_btn = new JButton("친구 초대"); //친구 초대 버튼
            exit_btn = new JButton("나가기"); //나가기버튼
            
            
            sp_ta.setBounds(10,10,380,390); 
            la_msg.setBounds(10,410,60,30); 
            sendTF.setBounds(70,410,320,30); 
            sp_list.setBounds(400,10,120,350); 
            invite_btn.setBounds(400,370,120,30); 
            exit_btn.setBounds(400,410,120,30);
            JPanel p=new JPanel();
            p.setLayout(null);
            p.setBackground(Color.GRAY);
            p.add(sp_ta);
            p.add(la_msg);
            p.add(sendTF);
            p.add(sp_list);
            p.add(invite_btn);
            p.add(exit_btn);
            
            sendTF.addActionListener(new ActionListener() { //채팅창에서 텍스트를 입력했을 때
              public void actionPerformed(ActionEvent e) {
                 out.println("chatting TEXT"+host+":"+curid+":"+sendTF.getText()); //프로토콜로 host와 현재 로그인 id 그리고 보낼 메시지를 서버로 보내준다
                 sendTF.setText("");
              }
            });
            
            exit_btn.addActionListener(new ActionListener() { //채팅방을 나갈 경우
              public void actionPerformed(ActionEvent e) {
                 if (e.getSource() == exit_btn)
                    out.println("Exit"+host+":"+curid); //나가는 채팅방의 host와 나가는 id를 서버로 보내준다
                    room_frame.dispose();
              }
            });
            invite_btn.addActionListener(new ActionListener(){ //친구 초대 버튼
           public void actionPerformed(ActionEvent e)
           {
              if(e.getSource()==invite_btn)
              {
                 new Invite(curid); //초대하려는 user를 검색하는 창을 열어준다
              }
           }
            });
            
           room_frame.add(p);
            room_frame.setBounds(300,200,550,500);
            room_frame.setVisible(true);
            room_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sendTF.requestFocus();    
          }
    }
    public class Withdrawal {
    	JFrame sub=new JFrame("Withdrawal");
    	JLabel idlbl, pwlbl;
    	JTextField idField;
    	JPasswordField passwd;
    	JPanel idPanel, paPanel;
    	JButton with_btn;
    	JTextArea content;
    	
    	public Withdrawal() {
    		sub.setLayout(new FlowLayout());
            EtchedBorder eborder =  new EtchedBorder();
            
            lbl = new JLabel( "Enter user information" );
            // 레이블에 영역 만들기
            lbl.setBorder(eborder);
            sub.add( lbl );
            
            idPanel = new JPanel();
            paPanel = new JPanel();
            
            idlbl = new JLabel("user Id        ");
            idField = new JTextField(10);
            idPanel.add(idlbl);
            idPanel.add(idField);
            idPanel.setBackground(Color.yellow);
            sub.add(idPanel);
            pwlbl = new JLabel("password  ");
            passwd = new JPasswordField(10);
            paPanel.add(pwlbl);
            paPanel.add(passwd);
            paPanel.setBackground(Color.yellow);
            sub.add(paPanel);
            
            with_btn = new JButton("Withdrawal");
            with_btn.setBackground(Color.yellow);
            sub.add(with_btn);
            
            with_btn.addActionListener(new ActionListener() 
            {
               public void actionPerformed(ActionEvent e) 
               {
                  String comment=e.getActionCommand();
                  if(comment.contentEquals("Withdrawal")) //register버튼 입력시,
                  {
                     String id=idField.getText();
                     String pw=passwd.getText();
                     String encryptPW = encryptSHA256(pw);
                     out.println("Withdrawal"+id+" "+encryptPW); //서버에게 전달.
                  }
                  sub.dispose();
               }
               });
          sub.setSize( 250, 200);
          sub.getContentPane().setBackground(Color.white);
          sub.setVisible(true);
          sub.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    		
    	}
    }
    
    public class Resister { //회원가입을 할 때의 창

        JFrame sub=new JFrame("Resister");
        JLabel lbl,namelbl,birthlbl,emaillbl, idlbl,pwlbl,nicklbl;
         JTextField nameField, birthField,emailField, idField, nickField;
         JPasswordField passwd;
         JPanel namePanel,birthPanel, emailPanel, idPanel,paPanel,nickPanel;
         JButton resister_btn;
         JTextArea content;
         
        public Resister() {
           
                sub.setLayout(new FlowLayout());
                
                EtchedBorder eborder =  new EtchedBorder();
   
                lbl = new JLabel( "Enter user information" );
                // 레이블에 영역 만들기
                lbl.setBorder(eborder);
                sub.add( lbl );

                namePanel = new JPanel();
                birthPanel = new JPanel();
                emailPanel = new JPanel();
                idPanel = new JPanel();
                paPanel = new JPanel();
                nickPanel = new JPanel();
                
                namelbl = new JLabel("User Name");
                nameField = new JTextField(10);
                namePanel.add(namelbl);
                namePanel.add(nameField);
                namePanel.setBackground(Color.yellow);
                sub.add(namePanel);
                
                birthlbl = new JLabel("birthday     ");
                birthField = new JTextField(10);
                birthPanel.add(birthlbl);
                birthPanel.add(birthField);
                birthPanel.setBackground(Color.yellow);
                sub.add(birthPanel);
                
                emaillbl = new JLabel("email          ");
                emailField = new JTextField(10);
                emailPanel.add(emaillbl);
                emailPanel.add(emailField);
                emailPanel.setBackground(Color.yellow);
                sub.add(emailPanel);
                idlbl = new JLabel("user Id        ");
                idField = new JTextField(10);
                idPanel.add(idlbl);
                idPanel.add(idField);
                idPanel.setBackground(Color.yellow);
                sub.add(idPanel);
                pwlbl = new JLabel("password  ");
                passwd = new JPasswordField(10);
                paPanel.add(pwlbl);
                paPanel.add(passwd);
                paPanel.setBackground(Color.yellow);
                sub.add(paPanel);
                nicklbl = new JLabel("nick name ");
                nickField = new JTextField(10);
                nickPanel.add(nicklbl);
                nickPanel.add(nickField);
                nickPanel.setBackground(Color.yellow);
                sub.add(nickPanel);
                

                resister_btn = new JButton("Resister");
                resister_btn.setBackground(Color.yellow);
                sub.add(resister_btn);

                resister_btn.addActionListener(new ActionListener() 
                  {
                     public void actionPerformed(ActionEvent e) 
                     {
                        String comment=e.getActionCommand();
                        if(comment.contentEquals("Resister")) //register버튼 입력시,
                        {
                           String name=nameField.getText(); //이름, 생년월일등을 불러옴
                           String bday=birthField.getText();
                           String email=emailField.getText();
                           String id=idField.getText();
                           String pw=passwd.getText();
                           String encryptPW = encryptSHA256(pw);
                           String nick = nickField.getText();
                           out.println("Resister"+name+" "+bday+" "+email+" "+id+" "+encryptPW + " " + nick); //서버에게 전달.
                        }
                        sub.dispose();
                     }
                     });
                sub.setSize( 250, 350 );
                sub.getContentPane().setBackground(Color.white);
                sub.setVisible(true);
                sub.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
     }
    public class Menu{ //로그인 승인 되었을 경우 나오는 창
    public Menu(String name, String comment, String friend) {
    	  ImageIcon background = new ImageIcon("펭수메뉴.png");
          JPanel contentPane = new JPanel() {
             public void paintComponent(Graphics g) {
                g.drawImage(background.getImage(), 0, 0, 400, 500, null);
                setOpaque(false);// 배경 띄워주기s
                super.paintComponent(g);
             }
          };
         
        popupMenu = new JPopupMenu(); //친구들을 표시해주는 팝업 메뉴
        lookinfo = new JMenuItem("친구정보보기"); //오른쪽 버튼 클릭시 친구정보보기와 채팅신청하기 창
        sendMsg = new JMenuItem("채팅신청하기");
        popupMenu.add(lookinfo);
        popupMenu.add(sendMsg);
       menu_frame = new JFrame("Menu");
       user_name = new JLabel(name);
       JLabel userNamelbl = new JLabel("NAME: "); //로그인 한 사람의 이름과 상태메시지를 표시해주는 label
       JLabel user_status_lbl = new JLabel("STATUS MESSAGE: ");
       user_state = new JLabel(comment);
       JLabel friends=new JLabel("Friends:"); //친구리스트에 대한 패널
       add_friend=new JButton("친구검색");
       friend_list = new JList(new DefaultListModel()); 
       friend_model = (DefaultListModel)friend_list.getModel(); //자신의 친구리스트에 대한 패널
       
       for(String add:friend.split(" ")) {
          friend_model.addElement(add);
       }
       friend_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
       friend_list.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
             // 오른쪽 버튼 클릭 시 ...
             if(e.getModifiers() == MouseEvent.BUTTON3_MASK) { // 왼쪽이 1 가운데가 2 오른쪽이 3   BUTTON3_MASK - 오른쪽 버튼
                // System.out.println("반응");
                popupMenu.show(friend_list, e.getX(), e.getY());
                 friend_list = (JList)e.getSource(); //오른쪽 버튼을 눌렀을 때 반응이 오게 해준다
             }
          }
       });
       
        lookinfo.addActionListener(new ActionListener() {  //친구정보보기에 대한 기능
              @Override
              public void actionPerformed(ActionEvent e) 
              {
               String comm=e.getActionCommand();
               if(comm.contentEquals("친구정보보기")) //버튼 클릭 시,
                  {
                  String str = (String)friend_list.getSelectedValue();  
                  out.println("Search"+str); //서버에게 정보보기를 원하는 사용자와 프로토콜을 보낸다.
                  }
              }
               });
        
        sendMsg.addActionListener(new ActionListener() { //친구한테 채팅요청을 하는 기능
              @Override
              public void actionPerformed(ActionEvent e) {
                 String start=e.getActionCommand();
                 if(start.contentEquals("채팅신청하기"))
                 {
                 new Chatting_Room(curid,curid,curid);   //해당 신청을 한 사용자만의 채팅방 개설
                 String receiver=(String)friend_list.getSelectedValue(); //채팅을 받을 사람을 가져온다
              out.println("Start:"+receiver+":"+curid); //채팅받는 사람과 채팅요청하는 사람의 프로토콜을 서버에 보내준다
              host=curid; //채팅요청을 하는 사람을 host로 만들어준다
                 }
              }
               });
       friend_model.remove(0); // db에 저장된 친구이름이 null로 시작해서 앞에 null 지워줌
       change_info = new JButton("정보 수정"); //자신의 정보를 수정해주는 버튼
      
      
       contentPane.setLayout(null);
       contentPane.add(add_friend);
       contentPane.add(change_info);
       contentPane.add(userNamelbl);
       contentPane.add(user_name);
       contentPane.add(user_status_lbl);
       contentPane.add(user_state);
       contentPane.add(friends);
       contentPane.add(friend_list);
       add_friend.setBounds(10, 10, 120, 30);
       change_info.setBounds(150, 10, 120,30);
       userNamelbl.setBounds(10, 60, 150, 20);
       user_name.setBounds(50, 60, 150, 20);
       user_status_lbl.setBounds(10, 90, 350, 15);
       user_state.setBounds(120, 90, 350, 15);
       friends.setBounds(10,100,200,15);
       friend_list.setBounds(10, 130, 350, 200);
       
       //동네예보 공공데이터 표출 (메뉴창에서)
       SocialDate s = new SocialDate();
       //socialDate 클래스에서 json parsing 한 성남시 기온과 습도를 가져온다
      
       JLabel locationlbl = new JLabel("성남시 수정구 기준시간 : 00시");
       locationlbl.setBounds(10,370,300,20);
       JLabel t1hlbl = new JLabel("기온");
       JLabel t1hlbl2 = new JLabel(s.tem);
       JLabel rehlbl = new JLabel("습도");
       JLabel rehlbl2 = new JLabel(s.hum);
       locationlbl.setForeground(Color.RED);
       t1hlbl.setBounds(10, 400, 150, 20);
       t1hlbl.setForeground(Color.RED);
       t1hlbl2.setBounds(60, 400, 150,20);
       t1hlbl2.setForeground(Color.RED);
       contentPane.add(t1hlbl);
       contentPane.add(t1hlbl2);
       rehlbl.setBounds(10, 430,150,20);
       rehlbl.setForeground(Color.RED);
       rehlbl2.setBounds(60, 430,150,20);
       rehlbl2.setForeground(Color.RED);
       contentPane.add(rehlbl);
       contentPane.add(rehlbl2);
       contentPane.add(locationlbl);
       contentPane.setSize(400,80);
       contentPane.setVisible(true);
       menu_frame.getContentPane().add(contentPane); 
     menu_frame.addWindowListener(new WindowListener() {
           @Override
           public void windowClosed(WindowEvent e) {
           }
           @Override
           public void windowOpened(WindowEvent e) {            
           }
           @Override
           public void windowClosing(WindowEvent e) {//메뉴창을 닫았을 때, 로그 아웃했다고 서버에게 전달하고 업데이트 됨. 최근 접속시간을 
               //String date   
               Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
                out.println("LOGOUT"+ sdf.format(date));
           }
           @Override
           public void windowIconified(WindowEvent e) {            
           }

           @Override
           public void windowDeiconified(WindowEvent e) {   
           }

           @Override
           public void windowActivated(WindowEvent e) {         
           }

           @Override
           public void windowDeactivated(WindowEvent e) {      
           }

            });
         add_friend.addActionListener(new ActionListener() { //친구 검색 버튼을 눌렀을 때
             @Override
             public void actionPerformed(ActionEvent e) 
             {
                      new Addfr();      //검색창이 나온다
             }
              });
         change_info.addActionListener(new ActionListener() { // 메뉴에서 정보변경 버튼 누르면 

           @Override
           public void actionPerformed(ActionEvent e) {
                 new ChangeInfo(); //변화시켜주는 창이 나온다
           }
         });
         
         menu_frame.setSize(400,500);
         menu_frame.setVisible(true);
         }

    }
          public class Invite //로그인해서, user를 검색하고, 초대를 할 수 있는 창
          {
             public Invite(String id)
             {
                JFrame iadder=new JFrame();
                iadder.setTitle("초대하실 id를 입력해주세요.");
               iadder.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                iadder.setLayout(new FlowLayout());
                JLabel inviid=new JLabel("아이디");
                JTextField iid=new JTextField(20);
                iadder.add(inviid);
                iadder.add(iid);
                iadder.setSize(300,150);
                iadder.setVisible(true);
                JButton invitefr=new JButton("초대");
                JPanel pnr=new JPanel();
                pnr.add(invitefr);
                iadder.add(pnr,BorderLayout.SOUTH);
          invitefr.addActionListener(new ActionListener() //text로 정보를 확인하고, 친구를 추가함.
                      {
                      public void actionPerformed(ActionEvent e)
                      {
                         String comment=e.getActionCommand();
                             if(comment.contentEquals("초대")) //초대 버튼을 눌렀을 때
                            {
                          String inviname=iid.getText(); //텍스트창에서 초대할 사람 아이디를 받아온다
                           out.println("Invite"+host+":"+inviname+":"+id); //서버에게 host이름과 초대할 사람, 초대하려는 요청자를 보내준다
                           iadder.dispose(); //창을 닫아준다
                            }
                      }
                   });
         
             }
          }
          public class Addfr //로그인해서, user를 검색하는창
          {
             public Addfr()
             {
                JFrame adder=new JFrame();
                adder.setTitle("친구 검색");
               adder.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                adder.setLayout(new FlowLayout());
                JLabel searid=new JLabel("아이디");
                JTextField uid=new JTextField(20);
                adder.add(searid);
                adder.add(uid);
                adder.setSize(300,150);
                adder.setVisible(true);
                JButton search=new JButton("검색");
                JButton add=new JButton("추가");
                JPanel pnr=new JPanel();
                pnr.add(search);
                pnr.add(add);
                adder.add(pnr,BorderLayout.SOUTH);
                search.addActionListener(new ActionListener() //검색할 userid를 작성하고, 검색을 함. 
                             {
                             public void actionPerformed(ActionEvent e)
                             {
                                String comment=e.getActionCommand();
                                    if(comment.contentEquals("검색"))  //검색버튼을 누를때,
                                    {
                                  String conveyid=uid.getText(); //텍스트창에서 문서를 받아온다
                                  out.println("Search"+conveyid);//서버에 검색하는 아이디를 전달.
                                    }
                             }
                          });      
                add.addActionListener(new ActionListener() //text로 정보를 확인하고, 친구를 추가함.
                            {
                            public void actionPerformed(ActionEvent e)
                            {
                               String comment=e.getActionCommand();
                                   if(comment.contentEquals("추가")) 
                                   {
                                 relaid=uid.getText(); //텍스트창에서 문서를 받아온다
                                 out.println("ADDF"+relaid);//서버에 추가하려는 id를 보낸다. 
                                   }
                            }
                         });      
              
             }  
             
             }
          public class ChangeInfo{ //내정보 변경하는 창
              public ChangeInfo() {
                 JFrame ch_frame=new JFrame();
                 JPanel ch_panel = new JPanel();
                 ch_panel.setLayout(null);

                 
                     ch_frame.setTitle("내 정보 변경");
                     
                     JLabel nicklbl2 = new JLabel( "Nick Name");
                     JTextField nickField2 = new JTextField(50); //바꿀 닉네임
                     
                     JLabel commentlbl = new JLabel("Comment");
                     JTextField commentField = new JTextField(50); // 바꿀 상태 메시지
                     
                     JButton ch_btn = new JButton("정보 변경"); // 정보 변경 버튼 누르면 바꾸고자 하는 정보가 반영됨
                     
                     nicklbl2.setBounds(10,10,100,30);
                     nickField2.setBounds(130, 10, 300,30);
                     ch_panel.add(nickField2);
                     ch_panel.add(nicklbl2);
                     
                     commentlbl.setBounds(10,80,100,30);
                     commentField.setBounds(130, 80, 300,30);
                     ch_panel.add(commentlbl);
                     ch_panel.add(commentField);
                     
                     ch_btn.setBounds(330,120,100,30);
                     ch_panel.add(ch_btn);
                     
                     ch_frame.add(ch_panel);
                     ch_frame.setSize(500,200);
                     ch_frame.setVisible(true);
                     
                     ch_btn.addActionListener(new ActionListener() {

                   @Override
                   public void actionPerformed(ActionEvent e) {
                      String ch_nick = nickField2.getText(); 
                      String ch_comment = commentField.getText();
                      out.println("ChangeInfo:"+ch_nick+":"+ch_comment);   // 바꿀 내용 서버에 알림
                       user_state.setText(ch_comment);   // 바꾼 내용을 메뉴 칸에 반영함
                      ch_frame.dispose();
                   }
                        
                     });
                     
                     
                     
                  
              }
              
           }
    //TODO:
 private void run() {
              try {
                 Socket socket = new Socket(serverAddress, portnum); //입력받은 ip주소와 portnumber로 소켓 생성.
                  in = new Scanner(socket.getInputStream()); //소켓에서 읽어오는 변수
                  out = new PrintWriter(socket.getOutputStream(), true); //소켓에 쓰는 변수
                  String[] userinfo = null; //사용자 정보저장하는 변수
                  while(true)
                  {
                     String line=in.nextLine();
                     if(line.contains("access")) //로그인한 정보를 서버가 검사하고, 승인됫다는 메시지가 왔을 경우
                     {
                        userinfo = line.split(":");
                        
                         JOptionPane.showMessageDialog(null, "로그인 성공", "로그인 확인!", JOptionPane.DEFAULT_OPTION);//서버가 로그인 성공이라고 했을 시, 창 표출
                         //TODO:
                        this.new Menu(userinfo[1],userinfo[2],userinfo[4]); //정보들을 바탕으로 메뉴 창 개설
                         curid=userinfo[3]; //현재아이디를 저장
                     }
                     // 아이디가 틀렸을 때
                     else if(line.contains("id invalid"))
                     {
                        JOptionPane.showMessageDialog(null, "아이디 틀림!", "로그인 확인!", JOptionPane.DEFAULT_OPTION); //로그인 실패 시, 창표출
                        return;
                     }
                     
                     // 비밀번호가 틀렸을 때
                     else if(line.contains("pw invalid"))
                     {
                        JOptionPane.showMessageDialog(null, "비밀번호 틀림!", "로그인 확인!", JOptionPane.DEFAULT_OPTION); //로그인 실패 시, 창표출
                        return;
                     }
                     else if(line.contains("Welcome")) //회원가입에 성공했을 때
                     {
                        JOptionPane.showMessageDialog(null, "회원가입성공", "환영합니다!", JOptionPane.DEFAULT_OPTION); //회원가입 성공 시, 창표출
                        return;
                     }
                     else if(line.contains("Searching")) //검색한 정보를 받았을 때,
                     {
                        String printinfo=line.substring(9); //정보 뽑아냄
                        String [] showbox=printinfo.split(" "); 
                        String wholeinfo="";
                        if(showbox[4].equals("1") == true) //접속정보 로그인이면 1 로그아웃이면 0으로 바꿔준다
                           showbox[4] = "on";
                        else
                           showbox[4] = "off";
                       
                        wholeinfo="name"+":"+showbox[0]+"\n"+"birthday"+":"+showbox[1]+"\n"+"email"+":"+showbox[2]+
                                "\n"+"comment"+":"+showbox[3]+"\n"+"status"+":"+showbox[4]+"\n"+"Off time"+":"+showbox[5]+" "+showbox[6];
                        JOptionPane.showMessageDialog(null, wholeinfo, "검색하신 정보는 위와 같습니다.", JOptionPane.DEFAULT_OPTION); 
                        //묶은 정보를 출력.
                   
                     }
                     else if(line.contains("ADD")) //친구 추가완료됨
                     {
                        JOptionPane.showMessageDialog(null, "친구추가완료", "축하합니다", JOptionPane.DEFAULT_OPTION); //친구추가된 경우, 창표출
                        friend_model.addElement(relaid);
                     }
                     else if(line.contains("DUPLICATION")) //이미 추가되어 있는 친구인 경우, 메시지전달
                     {
                        JOptionPane.showMessageDialog(null, "이미 추가된 친구입니다.", "친구 추가 실패",JOptionPane.DEFAULT_OPTION);// 친구가 중복된 경우
                     }
                     else if(line.contains("No user")) //server에서, 해당 사용자가 없다고 왓을 경우 오류 출력.
                     {
                        JOptionPane.showMessageDialog(null, "존재하지 않는 아이디입니다.", "error   ", JOptionPane.DEFAULT_OPTION); //친구명단을 임시로 창표출
                     }
                     else if(line.contains("Message")) //평소에 채팅방에서 메시지를 주고 받을 때
                     {
                        String msg = line.substring(7);
                        String [] comparator=msg.split(":");// host와 보내는 사람 메시지를 잘라준다
                        String hostcomp=comparator[0];//host
                        String chattalk=comparator[1]+":"+comparator[2];//채팅방에 전달할 메시지
                        if(hostcomp.equalsIgnoreCase(host)) //클라이언트의 호스트와 온 메시지의 호스트가 일치할때만 메시지 출력
                        {
                        sendTF.setEditable(true); //채팅창 활성화
                        ta.append(chattalk + "\n"); //메시지 표출시켜줌
                        }
                     }
                     else if(line.contains("Entrance")) //채팅요청을 승락해서 입장하는 경우
                     {
                        String etr=line.substring(8);
                        String []etra=etr.split(":"); //host이름과 입장메시지 분리
                        String hostreal=etra[0]; //host
                        if(hostreal.equalsIgnoreCase(host)) //해당 host인 채팅방인지 검사
                        {
                           if(line.contains("has joined")&&curid!=etra[1].split(" ")[0]) { 
                                 user_model.addElement(etra[1].split(" ")[0]);
                              } //입장한 것을 메시지로 표시 그리고 채팅 리스트에 더해준다
                        sendTF.setEditable(true); //입장했다는 신호를 알려줌 채팅방 활성화
                         ta.append(etra[1] + "\n");  //채팅메시지 표출
                        }
                     }
                    else if(line.contains("Request")) // 채팅요청이 왓을 경우 클라이언트에게
                    {
                       String ifo=line.substring(8);
                       String [] temp=ifo.split(":");
                            String rcv=temp[0]; //수신자와 송신자 분리
                            String snd=temp[1]; 
                       if(rcv.equalsIgnoreCase(curid)==true) //그 요청을 받은 해당 클라이언트인지 검사
                       {
                        int result=JOptionPane.showConfirmDialog(null, //채팅 수락 여부 메시지 표출 
                                "수락하시겠습니까?", snd+"으로부터 채팅요청이 왔습니다", 
                                JOptionPane.YES_NO_OPTION);
                            if(result == JOptionPane.NO_OPTION)
                            {
                               out.println("Nop"+temp[1]+":"+snd+" "+"reject the chatting");//채팅요청을 거부햇을 때, 거부메시지 전달
                               sendTF.setEditable(false);
                            }
                          else if(result == JOptionPane.YES_OPTION)
                          {
                             this.new Chatting_Room(rcv,snd,temp[1]); //채팅방을 수락햇을 경우, 수신자의 채팅방 생성
                             host=temp[1]; //host에 송신자를 넣어준다
                             out.println("Join"+host+":"+rcv); //합류했다는 사실을 서버에게 전달
                          }
                        }
                    }
                   else if(line.startsWith("Invite")) //초대한다는 메시지가 왓을경우
                   {
                      String ite=line.substring(6);
                      String [] tmp=ite.split(":");
                      if(curid.equalsIgnoreCase(tmp[1])==true) //현재 클라이언트랑 초대하려는 사람이 일치하는 경우
                      {
                       int result2=JOptionPane.showConfirmDialog(null,  //채팅 요청 메시지 창 표출
                               "수락하시겠습니까?",  tmp[2]+"으로부터 채팅초대가 왔습니다", 
                               JOptionPane.YES_NO_OPTION);
                           if(result2 == JOptionPane.NO_OPTION)
                           {
                              out.println("Nop"+tmp[0]+":"+tmp[1]+" "+"reject the chatting");//채팅 거부에 대한 메시지 전달
                              host=null; //host가 없으므로 null로 바꿔준다
                              sendTF.setEditable(false);
                           }
                         else if(result2 == JOptionPane.YES_OPTION)
                         {
                            //String instanceList = tmp[3].substring(1);
                          // for(string a : tmp[3]) 
                             //.out.println("tmp:" + tmp[3].split(",")[0]);
                            host=tmp[0];  //채팅창에서 host저장
                            out.println("Join"+host+":"+tmp[1]); //서버에 합류했다는 사실을 전달
                            this.new Chatting_Room(tmp[1],tmp[2],tmp[3]); //채팅방 생성
                         }
                       }
                   }
                       else if(line.contains("Denied")) //초대를 수락하지 않았을 때
                     {
                          String denmsg=line.substring(6);
                          String [] hcm=denmsg.split(":");
                          String hcomp=hcm[0]; //host 비교하는 변수
                          String text=hcm[1]; //메시지
                          if(hcomp.equalsIgnoreCase(host)) //전달하는 내용이 host가 일치하는 채팅방인지 확인
                          {
                          ta.append(text+"\n"); //호스트가 일치하는 채팅방에만 거부 메시지를 표출
                          }
                     }
                     else if(line.contains("Exit")) //채팅방을 나갈 경우
                    {
                       String exitinfo=line.substring(4);
                       String [] extract=exitinfo.split(":");
                       if(extract[0].equalsIgnoreCase(host)) //해당 채팅방의 host가 일치하는 지 확인
                       {
                    	   user_model.removeElement(extract[1].split(" ")[0]); //나갈 경우 제거
                          ta.append(extract[1]+extract[2]+"\n"); //호스트가 일치하는 경우에, 나갓다는 메시지 표출
                       }
                    }
                    }
                  }
              catch(Exception e)
              {
                 System.out.println(e);
              }
              finally {
                  frame.dispose();
              }
              }
    public static void server(String fileName) //input.txt파일에서 ip주소와 port number를 불러오는 함수.
          {  
            Scanner inputStream = null;
            try 
            {
               inputStream = new Scanner(new File(fileName));//input파일을 읽어옴.
            } 
            catch (FileNotFoundException e) //파일이 없을 경우, 자동생성
            {
               ip = "localhost";
               portnum = 9999; 
               e.printStackTrace();
            }
            
            while (inputStream.hasNext()) //input파일에서, ip주소와 portnumber를 읽어옴.
            {
               ip = inputStream.next(); 
               portnum = inputStream.nextInt();
            }
          }
    public static void main(String args[])
    {
        String fname="input.txt";
           server(fname); // input내용으로 정보 불러오기
           Login client=new Login(ip);
           client.frame.setVisible(true);
           client.run(); 
    }
}