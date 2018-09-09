import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Mutator {

	public static void main(String args[]) throws IOException {
		String sinfile = args[3];
		String soutfile = args[4];
		
		int gapsize = Integer.parseInt(args[5]);
		
		List<String> lines = new ArrayList<String>();
		lines.add("impl.addParameter(objectFlowState, parameter);");
		lines.add("return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();");
		lines.add("TestConstructor tc = new TestConstructor(\"saaa\");");
		lines.add("StreamResult sr = new StreamResult();");
		lines.add("if(DEBUG)System.out.println(\"reusing instance, object id : \" + fStreamWriter);");
		lines.add("g.fillOval(r.x, r.y, r.width-1, r.height-1);");
		lines.add("dummyAction.removePropertyChangeListener(arg0);");
		lines.add("throw new AssertionError(\"Trees.getOriginalType() error!\");");
		lines.add("oidStore.put(s.getName(), s) ;");
		lines.add("String sTypeVS = (String) lst.getSelectedItem();");
		lines.add("coords[nc++] = (x + ctrls[i + 0] * w + ctrls[i + 1] * aw);");
		lines.add("throw new UniqueFieldValueConstraintViolationException(classMetadata().getName(), fieldMetadata().getName());");
		lines.add("HardObjectReference ref = HardObjectReference.peekPersisted(trans, id, 1);");
		lines.add("org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [0];");
		lines.add("assertEquals(\"true\", buildRule.getProject().getProperty(\"error\"));");
		lines.add("block[block.length - 2]  = (byte)(len >> 8);");
		lines.add("factories.add(new PrintStreamProviderFactory(ps));");
		lines.add("column = (column / TabInc * TabInc) + TabInc;");
		lines.add("int lineStart = startPosition[getLineNumber(pos) - FIRSTLINE];");
		lines.add("((ComponentUI) (uis.elementAt(0))).getAccessibleChildrenCount(a);");
		Collections.shuffle(lines);
		
		List<String> original = new LinkedList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(sinfile)));
		String line;
		while((line = br.readLine()) != null) {
			original.add(line);
		}
		br.close();
		
		Random r = new Random();
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(soutfile)));
		int afterLine = r.nextInt(original.size());
		for(int i = 0; i < original.size(); i++) {
			bw.write(original.get(i) + "\n");
			if(i == afterLine) {
				for(int j = 0; j < gapsize; j++) {
					bw.write("\t" + lines.get(j) + "\n");
				}
			}
		}
		bw.flush();
		bw.close();
		
	}
	
}
