/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import Map.ControlPanel;
import Robot.Robot;
import Map.MapController;
import Map.MapFrame;
import Map.Map;
import Map.MapConstants;
import Map.MapPanel;
import Utils.CommMgr;
import java.awt.CardLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
/**
 *
 * @author denis
 */

public class mdpMain {
    
    private static MapFrame mapFrame;
    
    private static MapPanel mapPanel;
    private static MapPanel realMapPanel;
    //private static MapPanel exploredMapPanel;
    private static ControlPanel controlPanel;
    
    private static MapController m_control;
    
    private static Robot robot;
    
    private static Map realMap;
    private static Map exploredMap;
    
    private static int timeLimit = 3600;
    private static int coverageLimit = 300;
    
    private static final CommMgr comm = CommMgr.getCommMgr();
    private static final boolean realRun = false;
    
    public static void main(String[] args) {
        if(realRun){
            comm.openConnection();
            //comm.sendMsg("a", "INSTR");
        }
        
        robot = new Robot(MapConstants.START_ROW, MapConstants.START_COL, realRun);
        robot.setTimeLimit(timeLimit);
        robot.setCoverage(coverageLimit);
        
        initMap();
        displayEverything();
        
        if(robot.getIsRealRobot()){
            while (true) {
                System.out.println("Waiting for android command...");
                String msg = CommMgr.getCommMgr().recvMsg();
                if (msg.equals(CommMgr.EX_START)){
                    m_control.initExploration();
                }
            }
        }
//        if(!realRun){
//            realMap = new Map();
//        }
//        exploredMap = new Map();
//        exploredMap.setAllUnexplored();
        
//        displayEverything();
    }
    
    public static void initMap(){
//        if(!realRun){
//            realMap = new Map();
//        }
        realMap = new Map();
        exploredMap = new Map();
        exploredMap.setAllUnexplored();
        
    }

    public static void redisplay(){
        initMap();
        if(!realRun){
            mapPanel.setMap(realMap);
        } else{
            mapPanel.setMap(exploredMap);
        }
        mapFrame.setMapPanel(mapPanel);
        m_control.setMapPanel(mapPanel);
    }
    
    public static void loadMap(Map map){
        realMap = map;
        mapPanel.setMap(realMap);
        mapFrame.setMapPanel(mapPanel);
        m_control.setMapPanel(mapPanel);
    }

    private static void displayEverything(){
        if(!realRun){
            mapPanel = new MapPanel(realMap, robot);
        } else{
            mapPanel = new MapPanel(exploredMap, robot);
        }
        mapFrame = new MapFrame(mapPanel);
        //m_control = new MapController(mapFrame.getMapPanel(), mapFrame.getControlPanel(), robot);
        m_control = new MapController(mapFrame, robot);
        
    }
    
    
    public void startExploration(MapPanel mapPanel){
        mapPanel.setMap(exploredMap);
        ExplorationAlgo ex = new ExplorationAlgo(mapFrame, realMap, robot, robot.getCoverage(), robot.getTimeLimit());
        ex.runExploration();
    }
        
}
