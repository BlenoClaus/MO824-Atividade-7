package com.binpacking.solver;

import com.binpacking.io.InstanceReader;
import com.binpacking.model.BinPacking;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class BinPackingSolver {
	
	public GRBEnv env;
    public GRBModel model;
    public GRBVar[] y;
    public GRBVar[][] x;
    
    
    public void solver(BinPacking binPacking, String logName) throws GRBException {
    	env = new GRBEnv(logName+".log");
        model = new GRBModel(env);
        model.getEnv().set(GRB.DoubleParam.TimeLimit, 600.0);
        buildModel(binPacking);
        model.write(logName+".lp");
        model.optimize();
        model.dispose();
        env.dispose();
    }
	
	private void buildModel(BinPacking binPacking) throws GRBException {
		y = new GRBVar[binPacking.getNumberOfItems()];
		x = new GRBVar[binPacking.getNumberOfItems()][binPacking.getNumberOfItems()];
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			y[i] = model.addVar(0, 1, 0.0f, GRB.BINARY, "y[" + i + "]");
			for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
				x[i][j] = model.addVar(0, 1, 0.0f, GRB.BINARY, "x[" + i + "]["+j+"]");
			}
		}
		
		GRBLinExpr obj = new GRBLinExpr();
		for (int i = 0; i < binPacking.getNumberOfItems(); i++) {
			obj.addTerm(1.0, y[i]);
		}
		model.setObjective(obj, GRB.MINIMIZE);
		model.update();
		
		for (int i = 0 ; i < binPacking.getNumberOfItems(); i++) {
			for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
				GRBLinExpr expr = new GRBLinExpr();
		    	expr.addTerm(binPacking.getWeightOfItems().get(j), x[i][j]);
		    	GRBLinExpr exprResult = new GRBLinExpr();
		    	exprResult.addTerm(binPacking.getBinCapacity(), y[i]);
		    	model.addConstr(expr, GRB.LESS_EQUAL, exprResult, "");
			}
		}
		
		for (int j = 0; j < binPacking.getNumberOfItems(); j++) {
			GRBLinExpr expr = new GRBLinExpr();
			for (int i = 0 ; i < binPacking.getNumberOfItems(); i++) {
				expr.addTerm(1, x[i][j]);
			}
			model.addConstr(expr, GRB.EQUAL, 1, "");
		}
		
		model.update();
		model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);
		
	}

	public static void main(String[] args) {
		BinPacking bin0 = InstanceReader.build("instances/instance0.bpp");
		try {
			new BinPackingSolver().solver(bin0, "bin1");
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

}
