package com.backinfile.gen.proxy;

import com.backinfile.core.CallPoint;
import com.backinfile.core.Distr;
import com.backinfile.core.Port;
import com.backinfile.core.ProxyBase;
import com.backinfile.core.function.Action1;
import com.backinfile.core.function.Action2;
import com.backinfile.core.function.Action3;
import com.backinfile.core.function.Action4;
import com.backinfile.core.function.Action5;
import com.backinfile.core.function.Action6;
import com.backinfile.core.function.Action7;

<#list imports as import>
import ${import};
</#list>

public class ${className} extends ProxyBase {
	private CallPoint targetCallPoint;

	private ${className}(CallPoint targetCallPoint) {
		this.targetCallPoint = targetCallPoint;
		this.m_port = Port.getCurrentPort();
	}

<#if standalone>
	public ${className} newInstance() {
		return new ${className}(new CallPoint(Distr.getDefaultNodeId(), "${oriClassFullName}", 0L));
	}
<#else>
	public ${className} newInstance(long serviceId) {
		return new ${className}(new CallPoint(Distr.getDefaultNodeId(), "${oriClassFullName}", serviceId));
	}
</#if>

<#list methods as method>
<#if method.isPara>
    @SuppressWarnings({"rawtypes"}) 
</#if>
    public void ${method.name}(<#list method.args as arg>${arg.typeName} ${arg.name}${arg.dot}</#list>) {
		m_port.sendNewCall(targetCallPoint, ${method.methodKey}, new Object[] {<#list method.args as arg>${arg.name}${arg.dot}</#list>});
    }
    
</#list>
    @SuppressWarnings({"rawtypes"}) 
	public Object getMethod(int methodKey) {
		switch (methodKey) {
<#list methods as method>
		case ${method.methodKey}:
			return (Action${method.argsCount + 1}<${oriClassName}<#if method.argsCount gt 0>, </#if><#list method.args as arg>${arg.wrapName}${arg.dot}</#list>>) ${oriClassName}::${method.name};
</#list>
		default:
			break;
		}
		return null;
	}
}
