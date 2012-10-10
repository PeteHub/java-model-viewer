varying vec4 fvAmbient;
varying vec4 fvSpecular;
varying vec4 fvDiffuse;
varying float fSpecularPower;

varying vec3 ViewDirection;
varying vec3 LightDirection;
varying vec3 Normal;

void main( void )
{
   vec4  fvAmbient        = gl_LightSource[0].ambient  * gl_FrontMaterial.ambient;
   vec4  fvSpecular		  = gl_LightSource[0].specular * gl_FrontMaterial.specular;
   vec4	 fvDiffuse		  = gl_LightSource[0].diffuse  * gl_FrontMaterial.diffuse;
   float fSpecularPower	  = gl_FrontMaterial.shininess;

   vec3  fvLightDirection = normalize( LightDirection );
   vec3  fvNormal         = normalize( Normal );
   float fNDotL           = dot( fvNormal, fvLightDirection ); 
   
   vec3  fvReflection     = normalize( ( ( 2.0 * fvNormal ) * fNDotL ) - fvLightDirection ); 
   vec3  fvViewDirection  = normalize( ViewDirection );
   float fRDotV           = max( 0.0, dot( fvReflection, fvViewDirection ) );
   
   vec4  fvBaseColor      = vec4(1.0,1.0,1.0,1.0);
   
   vec4  fvTotalAmbient   = fvAmbient * fvBaseColor; 
   vec4  fvTotalDiffuse   = fvDiffuse * fNDotL * fvBaseColor; 
   vec4  fvTotalSpecular  = fvSpecular * ( pow( fRDotV, fSpecularPower ) );
  
   gl_FragColor = ( fvTotalAmbient + fvTotalDiffuse + fvTotalSpecular );
}