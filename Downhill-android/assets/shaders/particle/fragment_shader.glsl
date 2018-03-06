#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

uniform LOW vec3 u_color;

varying LOW float v_opacity;

void main()
{
	LOW float a = gl_PointCoord.x - 0.5;
	LOW float b = gl_PointCoord.y - 0.5;
	LOW float delta = 1.0 - ( a * a + b * b ) * 4.0;
	gl_FragColor = vec4 ( u_color , delta * v_opacity );
}
