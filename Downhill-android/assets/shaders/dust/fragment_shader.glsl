#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

varying LOW vec4 v_color;

void main()
{
	gl_FragColor = v_color;
}
