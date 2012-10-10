varying vec3 ViewDirection;
varying vec3 LightDirection;
varying vec3 Normal;
   
void main( void )
{
   gl_Position = ftransform();
    
   vec4 fvObjectPosition = gl_ModelViewMatrix * gl_Vertex;
   
   LightDirection = gl_LightSource[0].position.xyz - fvObjectPosition.xyz;
   ViewDirection  = vec3(0.0, 0.0, 0.0);
   Normal         = gl_NormalMatrix * gl_Normal;
   
}