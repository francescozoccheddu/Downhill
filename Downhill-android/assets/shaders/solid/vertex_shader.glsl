#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

attribute LOW vec2 a_position;
uniform HIGH mat4 u_projection;
uniform HIGH mat4 u_transform;

void main()
{
    gl_Position =  u_projection * u_transform * vec4 ( a_position , 0.0 , 1.0 );
}