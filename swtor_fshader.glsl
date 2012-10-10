varying vec3		LightDir;
varying vec3		EyeDir;
varying vec3		Normal;

uniform sampler2D	diffuseTexture;
uniform sampler2D	normalTexture;
uniform sampler2D	specularTexture;

void main()
{

	vec4 SurfaceColor	= texture2D(diffuseTexture, gl_TexCoord[0].st);
	vec4 BumpMapData	= texture2D(normalTexture, gl_TexCoord[0].st);
	vec4 SpecularData	= texture2D(specularTexture, gl_TexCoord[0].st);
	
	
	vec4 AmbientMatColor	= gl_LightSource[0].ambient * gl_FrontMaterial.ambient;
	vec4 DiffuseMatColor	= gl_LightSource[0].diffuse * gl_FrontMaterial.diffuse;
	vec4 SpecularMatColor	= gl_LightSource[0].specular * gl_FrontMaterial.specular;
	float specularFactor	= gl_FrontMaterial.shininess;
	
	vec4 vAmbient		= AmbientMatColor * SurfaceColor;	
	
	
	// Calculate bump map
	// In the normal map, the red and alpha channels are swapped
	// The alpha is also inverted
	
	vec2 bumpValue		= BumpMapData.ag*2.0-1.0;
	vec4 vBumpMap		= normalize(vec4(bumpValue.x, bumpValue.y, sqrt( 1- dot(bumpValue.xy, bumpValue.xy)), 1.0-BumpMapData.r));
	float bumpIntensity = max(dot(vBumpMap.xyz, LightDir), 0.0);
	
	vec4 vDiffuse		= DiffuseMatColor * SurfaceColor * bumpIntensity;
	

	// Calculate the specular
	
	vec3 reflectDir		= reflect(-LightDir, vBumpMap.xyz);
	
	float specularIntensity = max(dot(EyeDir, reflectDir), 0.0);	
	specularIntensity	= pow(1-specularIntensity, specularFactor) * SpecularData.a;
		
	vec4 vSpecular		= SpecularMatColor * SpecularData * specularIntensity;
	
	gl_FragColor		= vAmbient + vDiffuse + vSpecular;
}