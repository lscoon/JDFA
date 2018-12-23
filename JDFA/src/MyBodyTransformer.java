
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;

public class MyBodyTransformer extends BodyTransformer{

	public static int count = 0;
	@Override
	protected void internalTransform(Body arg0, String arg1, Map<String, String> arg2) {
		// TODO Auto-generated method stub
		count++;
	}

}
