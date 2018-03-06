#ifdef GL_ES 
#define HIGH highp
#define MED mediump
#define LOW lowp
#else
#define HIGH
#define MED
#define LOW
#endif

const LOW vec3 background_color = vec3 ( 1.0 , 1.0 , 1.0 );
const LOW float background_layer = -150.0;

attribute HIGH vec3 a_position;
attribute LOW float a_size;
attribute LOW float a_alpha;

uniform LOW float u_width;
uniform HIGH mat4 u_projection;
uniform LOW vec3 u_color;

varying LOW vec4 v_color;

void main()
{
	HIGH vec4 pos = u_projection * vec4 ( a_position , 1.0 );
    gl_Position = pos;
	gl_PointSize = a_size * u_width / pos.w;
	LOW float layer = a_position.z / background_layer;
	v_color = vec4 ( background_color * layer + u_color * ( 1.0 - layer ) , a_alpha );
}