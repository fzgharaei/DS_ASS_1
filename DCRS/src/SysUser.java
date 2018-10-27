public abstract class SysUser{
    String Identifier;
    User currUser;
    RMIDepartment deptInUse;

    public abstract void startProcess(String id);
}