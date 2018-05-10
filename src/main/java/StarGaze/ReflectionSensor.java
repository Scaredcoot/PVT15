package StarGaze;

public class ReflectionSensor {
	
	private int reflectionSensor;


public int getSensorData (double rainfall)
	{
		if (rainfall == 0){
			return reflectionSensor = 0;
		}
		if (rainfall > 0 && rainfall < 1.0){
			return reflectionSensor = 1;
		}
		if (rainfall > 1.0 && rainfall < 2.0){
			return reflectionSensor = 2;
		}
		if (rainfall > 2.0 && rainfall < 3.0){
			return reflectionSensor = 3;
		}
		
		else return reflectionSensor = 4;
	}
}
