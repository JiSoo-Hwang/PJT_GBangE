package com.kh.notice.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;
import com.kh.notice.model.vo.Attachment;
import com.kh.notice.model.vo.Notice;



public class NoticeDao {
	
	private Properties prop = new Properties();
	
	
	public NoticeDao() {
		
		String filePath = NoticeDao.class.getResource("/resources/sql/notice-mapper.xml").getPath();
		
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//글목록
	public ArrayList<Notice> selectNoticeList(Connection conn, PageInfo pi) {
		
		ArrayList<Notice> list = new ArrayList<>();
		ResultSet rset =null;
		PreparedStatement pstmt = null;
		String sql =prop.getProperty("selectNoticeList");
		
		int startRow = (pi.getCurrentPage()-1) * pi.getBoardLimit()+1;
		int endRow = pi.getCurrentPage() * pi.getBoardLimit();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rset =pstmt.executeQuery();
			
			while(rset.next()){
				list.add(new Notice(rset.getInt("NOTICE_ID")
								   ,rset.getString("NOTICE_TITLE")
								   ,rset.getString("MEMBER_NAME")
								   ,rset.getDate("CREATE_DATE")
								   ,rset.getInt("COUNT")));
								   
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return list;
	}
	//글 작성 
	public int insertNotice(Connection conn, Notice n,Attachment at) {
		 int result = 0;
		    PreparedStatement pstmt = null;
		    ResultSet rset = null;
		    String sql = prop.getProperty("insertNotice");

		    try {
		        // 회원 이름으로 회원 번호 찾기
		        String findMemberNoSql = "SELECT MEMBER_NO FROM MEMBER WHERE MEMBER_NAME = ?";
		        pstmt = conn.prepareStatement(findMemberNoSql);
		        pstmt.setString(1, n.getMemberName());
		        rset = pstmt.executeQuery();
		      

		        // 공지사항을 먼저 삽입
		        pstmt = conn.prepareStatement(sql);
		        pstmt.setString(1, n.getNoticeTitle());
		        pstmt.setInt(2, n.getMemberNo()); // 회원 번호 추가
		        pstmt.setString(3, n.getNoticeContent());
		        
		        result = pstmt.executeUpdate();
		        

				/*
				 * if (result > 0) { pstmt.close(); sql = prop.getProperty("insertAttachment");
				 * pstmt = conn.prepareStatement(sql); pstmt.setInt(1, at.getRefBno());
				 * pstmt.setString(2, at.getOriginName()); pstmt.setString(3,
				 * at.getChangeName()); pstmt.setString(4, at.getFilePath());
				 * 
				 * // 첨부파일 삽입 result = pstmt.executeUpdate(); }
				 */
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        JDBCTemplate.close(rset);
		        JDBCTemplate.close(pstmt);
		    }
		    return result;
		}
		
		
		
	

	//조회수 증가
	public int increaseCount(Connection conn, int nno) {
		 int result = 0;
	     PreparedStatement pstmt = null;
	     String sql = prop.getProperty("increaseCount");
	      
	     try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
			
		}
	     return result;    
	        
	}
	//공지글 상세보기
	public Notice selectNotice(Connection conn, int nno) {
		Notice n = null;//공지글 담을 객체변수
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			
			rset = pstmt.executeQuery();
			 
			if(rset.next()) {
				n = new Notice(rset.getInt("NOTICE_ID")
							  ,rset.getString("NOTICE_TITLE")
							  ,rset.getString("MEMBER_NAME")
							  ,rset.getString("NOTICE_CONTENT")
							  ,rset.getTimestamp("CREATE_DATE")
							  ,rset.getInt("COUNT"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return n;
	}
	
	//공지사항 제목 + 내용으로 검색기능
	public ArrayList<Notice> searchTitleContent(Connection conn, String searchType, String keyword) {
		 ArrayList<Notice> list = new ArrayList<>();
		    PreparedStatement pstmt = null;
		    ResultSet rset = null;
		    String sql = prop.getProperty("searchNotice"); 

		    try {
		       

		        pstmt = conn.prepareStatement(sql);
		        if (searchType.equals("titleContent")) {
		            pstmt.setString(1, "%" + keyword + "%");
		            pstmt.setString(2, "%" + keyword + "%");
		        } else {
		            pstmt.setString(1, "%" + keyword + "%");
		        	
		        }

		        rset = pstmt.executeQuery();

		        while (rset.next()) {
		            Notice notice = new Notice();
		            notice.setNoticeId(rset.getInt("NOTICE_ID"));
		            notice.setNoticeTitle(rset.getString("NOTICE_TITLE"));
		            notice.setNoticeContent(rset.getString("NOTICE_CONTENT"));
		            notice.setMemberName(rset.getString("MEMBER_NAME"));
		            notice.setCreateDate(rset.getTimestamp("CREATE_DATE"));
		            notice.setCount(rset.getInt("COUNT"));
		            list.add(notice);
		        }
		    } catch (SQLException e) {
		    	// TODO Auto-generated catch block
		        e.printStackTrace();
		    } finally {
		        JDBCTemplate.close(rset);
		        JDBCTemplate.close(pstmt);
		    }
		    return list;
	}
	//공지사항 내용으로 검색기능
	public ArrayList<Notice> searchContent(Connection conn, String searchType, String keyword) {
		 ArrayList<Notice> list = new ArrayList<>();
		    PreparedStatement pstmt = null;
		    ResultSet rset = null;
		    String sql = prop.getProperty("searchContent"); 

		    try {
		       

		        pstmt = conn.prepareStatement(sql);
		        if (searchType.equals("content")) {
		            pstmt.setString(1, "%" + keyword + "%");
		            pstmt.setString(2, "%" + keyword + "%");
		        } else {
		            pstmt.setString(1, "%" + keyword + "%");
		        	
		        }

		        rset = pstmt.executeQuery();

		        while (rset.next()) {
		            Notice notice = new Notice();
		            notice.setNoticeId(rset.getInt("NOTICE_ID"));
		            notice.setNoticeTitle(rset.getString("NOTICE_TITLE"));
		            notice.setNoticeContent(rset.getString("NOTICE_CONTENT"));
		            notice.setMemberName(rset.getString("MEMBER_NAME"));
		            notice.setCreateDate(rset.getTimestamp("CREATE_DATE"));
		            notice.setCount(rset.getInt("COUNT"));
		            list.add(notice);
		        }
		    } catch (SQLException e) {
		    	// TODO Auto-generated catch block
		        e.printStackTrace();
		    } finally {
		        JDBCTemplate.close(rset);
		        JDBCTemplate.close(pstmt);
		    }
		    return list;
	
		
		
		
		
	}
	//공지사항 제목으로 검색가능
	public ArrayList<Notice> searchTitle(Connection conn, String searchType, String keyword) {
		 ArrayList<Notice> list = new ArrayList<>();
		    PreparedStatement pstmt = null;
		    ResultSet rset = null;
		    String sql = prop.getProperty("searchContent"); 

		    try {
		       

		        pstmt = conn.prepareStatement(sql);
		        if (searchType.equals("title")) {
		            pstmt.setString(1, "%" + keyword + "%");
		            pstmt.setString(2, "%" + keyword + "%");
		        } else {
		            pstmt.setString(1, "%" + keyword + "%");
		        	
		        }

		        rset = pstmt.executeQuery();

		        while (rset.next()) {
		            Notice notice = new Notice();
		            notice.setNoticeId(rset.getInt("NOTICE_ID"));
		            notice.setNoticeTitle(rset.getString("NOTICE_TITLE"));
		            notice.setNoticeContent(rset.getString("NOTICE_CONTENT"));
		            notice.setMemberName(rset.getString("MEMBER_NAME"));
		            notice.setCreateDate(rset.getTimestamp("CREATE_DATE"));
		            notice.setCount(rset.getInt("COUNT"));
		            list.add(notice);
		        }
		    } catch (SQLException e) {
		    	// TODO Auto-generated catch block
		        e.printStackTrace();
		    } finally {
		        JDBCTemplate.close(rset);
		        JDBCTemplate.close(pstmt);
		    }
		    return list;
	
}
	
	//첨부파일 추가
	public int insertAttachment(Connection conn, Attachment at) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, at.getRefBno());
			pstmt.setString(2, at.getOriginName());
			pstmt.setString(3, at.getChangeName());
			pstmt.setString(4, at.getFilePath());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	public Attachment selectAttachment(Connection conn, int nno) {
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectAttachment");
		Attachment at = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				at = new Attachment(
						rset.getInt("FILE_NO")
						,rset.getString("ORIGIN_NAME")
						,rset.getString("CHANGE_NAME")
						,rset.getString("FILE_PATH")
						);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return at;
	}

	public int updateNotice(Connection conn, Notice notice) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateNotice");
		
		try {
			
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, notice.getNoticeTitle());
	        pstmt.setString(2, notice.getNoticeContent());
	        pstmt.setInt(3, notice.getNoticeId());
	        
	        
	        
	        result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int updateAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getFileNo());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int deleteNotice(Connection conn, int noticeId) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteNotice");
		
		try {
			pstmt =conn.prepareStatement(sql);
			pstmt.setInt(1,noticeId);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
			
		}
		
		return result;
	}
	
	
	


	

}

	
				
				
				

	

