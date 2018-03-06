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
attribute LOW vec2 a_texCoord;
varying LOW vec2 v_texCoord;

void main()
{
   v_texCoord = a_texCoord;
   gl_Position = vec4 ( a_position , 0.0 , 1.0 );
}
        