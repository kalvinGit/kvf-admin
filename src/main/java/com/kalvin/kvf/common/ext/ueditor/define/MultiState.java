package com.kalvin.kvf.common.ext.ueditor.define;

import com.kalvin.kvf.common.ext.ueditor.Encoder;

import java.util.*;

/**
 * 多状态集合状态
 * 其包含了多个状态的集合, 其本身自己也是一个状态
 * @author hancong03@baidu.com
 *
 */
public class MultiState implements com.kalvin.kvf.common.ext.ueditor.define.State {

	private boolean state = false;
	private String info = null;
	private Map<String, Long> intMap = new HashMap<String, Long>();
	private Map<String, String> infoMap = new HashMap<String, String>();
	private List<String> stateList = new ArrayList<String>();
	
	public MultiState ( boolean state ) {
		this.state = state;
	}
	
	public MultiState ( boolean state, String info ) {
		this.state = state;
		this.info = info;
	}
	
	public MultiState ( boolean state, int infoKey ) {
		this.state = state;
		this.info = com.kalvin.kvf.common.ext.ueditor.define.AppInfo.getStateInfo( infoKey );
	}
	
	@Override
	public boolean isSuccess() {
		return this.state;
	}
	
	public void addState ( State state ) {
		stateList.add( state.toJSONString() );
	}

	/**
	 * 该方法调用无效果
	 */
	@Override
	public void putInfo(String name, String val) {
		this.infoMap.put(name, val);
	}

	@Override
	public String toJSONString() {
		
		String stateVal = this.isSuccess() ? com.kalvin.kvf.common.ext.ueditor.define.AppInfo.getStateInfo( AppInfo.SUCCESS ) : this.info;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append( "{\"state\": \"" + stateVal + "\"" );
		
		// 数字转换
		Iterator<String> iterator = this.intMap.keySet().iterator();
		
		while ( iterator.hasNext() ) {
			
			stateVal = iterator.next();
			
			builder.append( ",\""+ stateVal +"\": " + this.intMap.get( stateVal ) );
			
		}
		
		iterator = this.infoMap.keySet().iterator();
		
		while ( iterator.hasNext() ) {
			
			stateVal = iterator.next();
			
			builder.append( ",\""+ stateVal +"\": \"" + this.infoMap.get( stateVal ) + "\"" );
			
		}
		
		builder.append( ", list: [" );
		
		
		iterator = this.stateList.iterator();
		
		while ( iterator.hasNext() ) {
			
			builder.append( iterator.next() + "," );
			
		}
		
		if ( this.stateList.size() > 0 ) {
			builder.deleteCharAt( builder.length() - 1 );
		}
		
		builder.append( " ]}" );

		return Encoder.toUnicode( builder.toString() );

	}

	@Override
	public void putInfo(String name, long val) {
		this.intMap.put( name, val );
	}

}
