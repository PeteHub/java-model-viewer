varying	vec3	LightDir;
varying	vec3	EyeDir;

attribute vec3	tangent;

void main()
{
	EyeDir			= vec3(gl_ModelViewMatrix * gl_Vertex);
	gl_Position		= ftransform();
	gl_TexCoord[0]	= gl_MultiTexCoord0;
	
	// Create a surface-local transformation matrix (well, 3 row vecros in this case)
	// The three vectors are the normal, tangent and binormal vectors
	// With these we can transform vertices to surface-local space (tangent space)
	vec3 n = normalize(gl_NormalMatrix * gl_Normal);
	vec3 t = normalize(gl_NormalMatrix * tangent);
	vec3 b = cross(n, t);
	
	// Calcualte the surface-local direction to the light
	vec3 v;
	vec3 LightPosition = gl_LightSource[0].position.xyz;
	v.x = dot(LightPosition, t);
	v.y = dot(LightPosition, b);
	v.z = dot(LightPosition, n);
	LightDir = normalize(v);
	
	// Calcualte the surface-local direction to the eye (viewing direction)
	v.x = dot(EyeDir, t);
	v.y = dot(EyeDir, b);
	v.z = dot(EyeDir, n);
	EyeDir = normalize(v);
}