package be.ac.ua.ansymo.cheopsj.distiller.popup.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.evolizer.changedistiller.model.classifiers.java.JavaEntityType;
import org.evolizer.changedistiller.model.entities.SourceCodeChange;
import org.evolizer.changedistiller.model.entities.SourceCodeEntity;

import be.ac.ua.ansymo.cheopsj.changerecorders.ClassRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.FieldRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.LocalVariableRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.MethodInvocationRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.MethodRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.PackageRecorder;
import be.ac.ua.ansymo.cheopsj.changerecorders.StatementRecorder;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.changes.Add;
import be.ac.ua.ansymo.cheopsj.model.changes.AtomicChange;
import be.ac.ua.ansymo.cheopsj.model.changes.Remove;

public class ChangeExtractor {

	private String changeIntent;
	private Date changeDate;
	private String changeUser;

	public ChangeExtractor(String message, Date date, String user) {
		changeIntent = message;
		changeDate = date;
		changeUser = user;
		System.out.println("BYPASSED!");
	}

	//#############################
	//###### CHANGEDISTILLER ######
	//#############################

	public void convertChanges(List<SourceCodeChange> sourceCodeChanges) {
		
	}

	//#############################
	//######## REMOVALS ###########
	//#############################

	public void storeMethodInvocationRemovals(String contents) {
		
	}

	public void storeMethodRemoval(String contents) {
		
	}

	public void storeFieldRemovals(String contents) {
		
	}

	public void storeClassRemoval(String contents) {
		
	}

	private void storePackageRemoval(PackageDeclaration pack) {
		// TODO Auto-generated method stub
		//Somehow find out how many types are declared in this Package.

	}

	//#############################
	//######## ADDITIONS ##########
	//#############################

	public void storeFieldAdditions(String contents) {
		
	}

	public void storeMethodAdditions(String contents) {
		
	}

	public void storeLocalVariableAdditions(MethodDeclaration method){
		
	}

	public void storeMethodInvocationAdditions(String contents) {
		
	}

	public void storeClassAddition(String contents) {
		
	}

	private TypeDeclaration getDeclaredType(String contents){
		CompilationUnit cu = getASTFromString(contents);
		List<?> types = cu.types();
		if(types.get(0) instanceof TypeDeclaration){
			return (TypeDeclaration) types.get(0);
		}else
			return null;
	}

	private void storePackageAddition(PackageDeclaration pack) {
		

	}

	//#############################
	//########## UTILS ############
	//#############################

	private static final java.sql.Timestamp utilDateToSqlTimestamp(java.util.Date utilDate) {
		return new java.sql.Timestamp(utilDate.getTime());
	}

	private CompilationUnit getASTFromString(String str) {
		CompilationUnit cu = null;

		ASTParser parser = ASTParser.newParser(AST.JLS3);

		parser.setSource(str.toString().toCharArray());
		cu = (CompilationUnit) parser.createAST(null);

		return cu;
	}

	private AtomicChange createAddition() {
		AtomicChange add = new Add();
		add.setIntent(changeIntent);
		add.setTimeStamp(utilDateToSqlTimestamp(changeDate));
		add.setUser(changeUser);
		return add;
	}

	private Remove createRemoval() {
		Remove rem = new Remove();
		rem.setIntent(changeIntent);
		rem.setTimeStamp(utilDateToSqlTimestamp(changeDate));
		rem.setUser(changeUser);
		return rem;
	}

	private class MIVisitor extends ASTVisitor {

		private List<MethodInvocation> invocations = new ArrayList<MethodInvocation>();

		@Override
		public boolean visit(MethodInvocation node) {
			invocations.add(node);
			return true;
		}

		public List<MethodInvocation> getMethodInvocations() {
			return invocations;			
		}
	}
}
