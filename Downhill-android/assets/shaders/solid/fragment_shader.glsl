#ifdef GL_ES 
#define LOW lowp
#else
#define LOW
#endif

uniform LOW vec4 u_color;

void main()
{
	gl_FragColor = u_color;
}
