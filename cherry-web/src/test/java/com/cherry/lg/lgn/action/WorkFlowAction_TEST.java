package com.cherry.lg.lgn.action;

import org.junit.Test;

import com.cherry.CherryJunitBase;
import com.cherry.lg.top.action.WorkFlowAction;

public class WorkFlowAction_TEST extends CherryJunitBase{
    private WorkFlowAction action;
    
    @Test
    public void testDoTask1() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "SD");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","PRM");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("100",action.doTask());
    }
    
    @Test
    public void testDoTask2() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "BG");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","PRM");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("101",action.doTask());
    }
    
    @Test
    public void testDoTask3() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "OD");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("200",action.doTask());
    }
    
    @Test
    public void testDoTask4() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "SD");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("201",action.doTask());
    }
    
    @Test
    public void testDoTask5() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "LS");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("202",action.doTask());
    }
    
    @Test
    public void testDoTask6() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "CA");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("203",action.doTask());
    }
    
    @Test
    public void testDoTask7() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "MV");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("204",action.doTask());
    }
    
    @Test
    public void testDoTask8() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "GR");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("205",action.doTask());
    }
    
    @Test
    public void testDoTask9() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "RR");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("206",action.doTask());
    }
    
    @Test
    public void testDoTask10() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "RA");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("207",action.doTask());
    }
    
    @Test
    public void testDoTask11() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "CR");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("208",action.doTask());
    }
    
    @Test
    public void testDoTask12() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "MV");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","PRM");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("102",action.doTask());
    }
    
    @Test
    public void testDoTask13() throws Exception{
        request.addParameter("OS_EntryID", "1");
        request.addParameter("OS_BillType", "BG");
        request.addParameter("OS_BillID", "1");
        request.addParameter("OS_ProType","N");
        action = createAction(WorkFlowAction.class, "/","TODOPROCESSINSTANCE");
        action.setServletRequest(request);
        assertEquals("209",action.doTask());
    }
}
